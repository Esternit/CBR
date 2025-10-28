// app/page.tsx
"use client";

import { useState, useEffect } from "react";
import { useForm, Controller } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import * as z from "zod";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { fetchCurrencies } from "@/lib/api";
import type { Currency } from "@/types/currency";
import { SearchableSelect } from "@/components/ui/searchable-select";
import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert";
import { Terminal } from "lucide-react";
import { CheckCircle2 } from "lucide-react";

const formSchema = z.object({
  fromAmount: z.string().optional(),
  toAmount: z.string().optional(),
  fromCurrencyId: z.number(),
  toCurrencyId: z.number(),
});

type FormValues = z.infer<typeof formSchema>;

export default function CurrencyConverter() {
  const [currencies, setCurrencies] = useState<Currency[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const load = async () => {
      try {
        const data = await fetchCurrencies();
        setCurrencies(data);
      } catch (err) {
        setError("Ошибка загрузки валют");
        console.error(err);
      } finally {
        setLoading(false);
      }
    };
    load();
  }, []);

  const currencyItems = currencies.map((currency) => ({
    value: currency.id.toString(),
    label: `${currency.name} (${currency.charCode})`,
  }));

  const [resultMessage, setResultMessage] = useState<string | null>(null);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);

  const form = useForm<FormValues>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      fromAmount: "",
      toAmount: "",
      fromCurrencyId: NaN,
      toCurrencyId: NaN,
    },
  });

  const { control, handleSubmit, setValue } = form;

  const onSubmit = (data: FormValues) => {
    setErrorMessage(null);
    setResultMessage(null);

    const fromAmount = data.fromAmount?.trim()
      ? parseFloat(data.fromAmount)
      : null;
    const toAmount = data.toAmount?.trim() ? parseFloat(data.toAmount) : null;

    const fromCurrency = currencies.find((c) => c.id === data.fromCurrencyId);
    const toCurrency = currencies.find((c) => c.id === data.toCurrencyId);

    if (!fromCurrency || !toCurrency) {
      setErrorMessage("Выберите обе валюты");
      return;
    }

    if (
      (fromAmount !== null && toAmount !== null) ||
      (fromAmount === null && toAmount === null)
    ) {
      setErrorMessage("Заполните ровно одно поле для конвертации");
      return;
    }

    const rubFrom =
      fromAmount !== null
        ? (fromAmount * fromCurrency.value) / fromCurrency.nominal
        : (toAmount! * toCurrency.value) / toCurrency.nominal;

    let result: number;
    if (fromAmount !== null) {
      result = (rubFrom * toCurrency.nominal) / toCurrency.value;
      setValue("toAmount", result.toFixed(4));
      setResultMessage(
        `${fromAmount} ${fromCurrency.name} = ${result.toFixed(4)} ${
          toCurrency.name
        }`
      );
    } else {
      result = (rubFrom * fromCurrency.nominal) / fromCurrency.value;
      setValue("fromAmount", result.toFixed(4));
      setResultMessage(
        `${toAmount} ${toCurrency.name} = ${result.toFixed(4)} ${
          fromCurrency.name
        }`
      );
    }
  };

  if (loading) return <div className="p-8 text-center">Загрузка валют...</div>;
  if (error) return <div className="p-8 text-center text-red-500">{error}</div>;

  return (
    <div className="min-h-screen bg-gray-50 flex items-center justify-center p-4">
      <Card className="w-full max-w-2xl">
        <CardHeader>
          <CardTitle className="text-2xl font-bold text-center">
            Конвертер валют
          </CardTitle>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div className="space-y-2">
                <label className="text-sm font-medium">Из валюты</label>
                <Controller
                  name="fromCurrencyId"
                  control={control}
                  render={({ field }) => (
                    <SearchableSelect
                      value={field.value?.toString() || ""}
                      onChange={(value) => field.onChange(Number(value))}
                      placeholder="Выберите валюту"
                      items={currencyItems}
                    />
                  )}
                />
              </div>

              <div className="space-y-2">
                <label className="text-sm font-medium">Сумма</label>
                <Controller
                  name="fromAmount"
                  control={control}
                  render={({ field }) => (
                    <Input
                      type="number"
                      step="any"
                      placeholder="0.00"
                      {...field}
                      onChange={(e) => field.onChange(e.target.value)}
                    />
                  )}
                />
              </div>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div className="space-y-2">
                <label className="text-sm font-medium">В валюту</label>
                <Controller
                  name="toCurrencyId"
                  control={control}
                  render={({ field }) => (
                    <SearchableSelect
                      value={field.value?.toString() || ""}
                      onChange={(value) => field.onChange(Number(value))}
                      placeholder="Выберите валюту"
                      items={currencyItems}
                    />
                  )}
                />
              </div>

              <div className="space-y-2">
                <label className="text-sm font-medium">Сумма</label>
                <Controller
                  name="toAmount"
                  control={control}
                  render={({ field }) => (
                    <Input
                      type="number"
                      step="any"
                      placeholder="0.00"
                      {...field}
                      onChange={(e) => field.onChange(e.target.value)}
                    />
                  )}
                />
              </div>
            </div>

            {errorMessage && (
              <Alert variant="destructive">
                <Terminal className="h-4 w-4" />
                <AlertTitle>Ошибка</AlertTitle>
                <AlertDescription>{errorMessage}</AlertDescription>
              </Alert>
            )}

            {resultMessage && (
              <Alert variant="default" className="border-green-500 bg-green-50">
                <CheckCircle2 className="h-4 w-4 text-green-600" />
                <AlertTitle className="text-green-700">Результат</AlertTitle>
                <AlertDescription className="text-green-600">
                  {resultMessage}
                </AlertDescription>
              </Alert>
            )}

            <Button type="submit" className="w-full btn-press">
              Пересчитать
            </Button>
          </form>
        </CardContent>
      </Card>
    </div>
  );
}

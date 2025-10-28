// components/ui/searchable-select.tsx
"use client";

import * as React from "react";
import { CaretSortIcon, CheckIcon } from "@radix-ui/react-icons";
import { cn } from "@/lib/utils";
import { Button } from "@/components/ui/button";
import {
  Command,
  CommandEmpty,
  CommandGroup,
  CommandInput,
  CommandItem,
  CommandList,
} from "@/components/ui/command";
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from "@/components/ui/popover";

interface SearchableSelectProps {
  value?: string;
  onChange: (value: string) => void;
  placeholder?: string;
  items: { value: string; label: string }[];
}

export function SearchableSelect({
  value,
  onChange,
  placeholder = "Выберите...",
  items,
}: SearchableSelectProps) {
  const [open, setOpen] = React.useState(false);
  const [search, setSearch] = React.useState("");

  const selectedItem = items.find((item) => item.value === value);

  // Фильтруем вручную
  const filteredItems = React.useMemo(() => {
    return items.filter((item) =>
      item.label.toLowerCase().includes(search.toLowerCase())
    );
  }, [items, search]);

  return (
    <Popover open={open} onOpenChange={setOpen}>
      <PopoverTrigger asChild>
        <Button
          variant="outline"
          role="combobox"
          aria-expanded={open}
          className="w-full justify-between"
        >
          {selectedItem ? selectedItem.label : placeholder}
          <CaretSortIcon className="ml-2 h-4 w-4 shrink-0 opacity-50" />
        </Button>
      </PopoverTrigger>
      <PopoverContent className="w-full p-0" align="start">
        <Command shouldFilter={false}>
          {" "}
          {/* ← отключаем встроенную фильтрацию */}
          <CommandInput
            placeholder={`Поиск...`}
            className="h-9"
            value={search}
            onValueChange={setSearch}
          />
          <CommandList>
            <CommandEmpty>Ничего не найдено.</CommandEmpty>
            <CommandGroup>
              {filteredItems.map((item) => (
                <CommandItem
                  key={item.value}
                  value={item.value}
                  onSelect={(currentValue) => {
                    onChange(currentValue === value ? "" : currentValue);
                    setOpen(false);
                    setSearch(""); // сброс поиска после выбора
                  }}
                >
                  {item.label}
                  <CheckIcon
                    className={cn(
                      "ml-auto h-4 w-4",
                      value === item.value ? "opacity-100" : "opacity-0"
                    )}
                  />
                </CommandItem>
              ))}
            </CommandGroup>
          </CommandList>
        </Command>
      </PopoverContent>
    </Popover>
  );
}

import { Currency } from '@/types/currency';

const API_BASE = 'http://localhost:8080/api/currencies'; 

export const fetchCurrencies = async (): Promise<Currency[]> => {
  const res = await fetch(API_BASE);
  if (!res.ok) throw new Error('Failed to fetch currencies');
  return res.json();
};
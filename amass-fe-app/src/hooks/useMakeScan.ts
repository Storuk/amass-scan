import { useMutation } from "@tanstack/react-query";
import { ScanResponse } from "../types";
import { scanDomain } from "../api";

const useMakeScan = () => {
  const { mutate, isError, data, error } = useMutation<
    ScanResponse, 
    Error, 
    string
  >({
    mutationFn: async (domain: string) => await scanDomain(domain),
  });

  return { mutate, isError, data, error };
};

export default useMakeScan;
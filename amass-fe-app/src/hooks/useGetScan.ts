import { useQuery } from "@tanstack/react-query";
import { getScan } from "../api";
import { ScanResponse } from "../types";

const useGetScan = (
  domain: string | undefined,
  version: number | undefined,
  isCalled: boolean
) => {
  const { isLoading, isError, data, error, refetch } = useQuery<
    ScanResponse | undefined
  >({
    queryKey: ["scan-data", domain, version],
    queryFn: async () => await getScan(domain, version),
    enabled: isCalled && !!domain && !!version,
  });

  return { isLoading, isError, data, error, refetch };
};

export default useGetScan;

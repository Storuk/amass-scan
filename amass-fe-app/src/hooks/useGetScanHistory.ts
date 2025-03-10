import { useQuery } from "@tanstack/react-query";
import { getScanHistory } from "../api";
import { ScanResponse } from "../types";

const useGetScanHistory = () => {
  const { isLoading, isError, data, error } = useQuery<ScanResponse[]>({
    queryKey: ["scan-history-data"],
    queryFn: async () => await getScanHistory(),
  });

  return { isLoading, isError, data, error };
};

export default useGetScanHistory;

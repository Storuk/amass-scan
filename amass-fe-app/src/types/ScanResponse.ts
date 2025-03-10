export type ScanResponse = {
  id: string;
  domain: string;
  startTime: string;
  endTime: string | null;
  status: "PROCESSING" | "COMPLETED" | "FAILED" | "NOT_FOUND";
  subdomains: string[] | null;
  ipAddresses: string[] | null;
  emailServers: string[] | null;
  dnsServers: string[] | null;
  version: number;
};
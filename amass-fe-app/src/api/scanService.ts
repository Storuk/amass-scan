import { ScanResponse } from "../types";

const BASE_API_PATH = import.meta.env.VITE_BASE_API_PATH;

const scanDomain = async (domain: string): Promise<ScanResponse> => {
  console.log(domain);
  const response = await fetch(`${BASE_API_PATH}?domain=${domain}`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
  });

  if (!response.ok) {
    throw new Error(`Failed to create scan: ${response.statusText}`);
  }

  return response.json();
};

const getScan = async (
  domain: string | undefined,
  version: number | undefined
): Promise<ScanResponse | undefined> => {
  // Return undefined if either domain or version is undefined
  if (domain === undefined || version === undefined) {
    return undefined;
  }

  // Proceed with the API call if both parameters are defined
  const response = await fetch(
    `${BASE_API_PATH}/domain/${domain}/version/${version}`,
    {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    }
  );

  if (!response.ok) {
    throw new Error(`Failed to create scan: ${response.statusText}`);
  }

  return response.json();
};

const getScanHistory = async (): Promise<ScanResponse[]> => {
  const response = await fetch(BASE_API_PATH, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
    },
  });

  if (!response.ok) {
    throw new Error(`Failed to create scan: ${response.statusText}`);
  }

  return response.json();
};

export { scanDomain, getScan, getScanHistory };

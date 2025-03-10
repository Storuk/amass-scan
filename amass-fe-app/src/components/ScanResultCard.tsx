import React, { useState } from "react";
import { ScanResponse } from "../types";
import { FaSync } from "react-icons/fa";
import useGetScan from "../hooks/useGetScan";

interface ScanResultCardProps {
  scanResponse: ScanResponse;
}

const ScanResultCard: React.FC<ScanResultCardProps> = ({ scanResponse }) => {
  const [additionalDetailsOption, setAdditionalDetailsOption] =
    useState<boolean>(false);
  const [isRefreshed, setIsRefreshed] = useState<boolean>(false);
  const { data: refreshedScan, refetch } = useGetScan(
    scanResponse?.domain,
    scanResponse?.version,
    isRefreshed
  );

  const scanData = refreshedScan || scanResponse;

  const onClickRefresh = () => {
    refetch();
    setIsRefreshed(true);
  };

  return (
    <div className="bg-white shadow-md border border-gray-300 rounded-lg p-5 w-72 mt-2">
      <div className="flex justify-between items-center">
        {/* Basic Scan Info */}
        <div>
          <p className="text-lg font-semibold text-gray-800">
            {scanData.domain}
          </p>
          <p
            className={`text-sm font-medium ${
              scanData.status === "COMPLETED"
                ? "text-green-600"
                : scanData.status === "PROCESSING"
                  ? "text-yellow-600"
                  : scanData.status === "FAILED"
                    ? "text-red-600"
                    : scanData.status === "NOT_FOUND"
                      ? "text-blue-600"
                      : "text-gray-600"
            }`}
          >
            {scanData.status}
          </p>

          <p className="text-xs text-gray-600">Start: {scanData.startTime}</p>
          {scanData.endTime && (
            <p className="text-xs text-gray-600">End: {scanData.endTime}</p>
          )}
        </div>

        {/* Refresh Button with Icon */}
        <button
          className="p-2 rounded-lg hover:bg-gray-100 focus:outline-none"
          onClick={onClickRefresh}
        >
          <FaSync className="w-5 h-5 text-blue-500 hover:text-blue-600" />
        </button>
      </div>

      {/* Show More / Hide Details */}
      <button
        onClick={() => setAdditionalDetailsOption((prev) => !prev)}
        className="mt-2 text-blue-500 hover:text-blue-700 text-sm font-medium focus:outline-none"
      >
        {additionalDetailsOption ? "Hide details ▲" : "More... ▼"}
      </button>

      {/* Additional Details (Hidden/Shown) */}
      {additionalDetailsOption && (
      <div className="mt-2 text-xs text-gray-700 border-t border-gray-200 pt-2 max-h-72 overflow-auto space-y-2">
        {scanData?.subdomains && (
          <p>
            <strong>Subdomains:</strong> {scanData.subdomains.map((subdomain, index) => (
              <span key={index} className="block">{subdomain}</span>
            ))}
          </p>
        )}
        {scanData?.ipAddresses && (
          <p>
            <strong>IpAddresses:</strong> {scanData.ipAddresses.map((ip, index) => (
              <span key={index} className="block">{ip}</span>
            ))}
          </p>
        )}
        {scanData?.emailServers && (
          <p>
            <strong>EmailServers:</strong> {scanData.emailServers.map((email, index) => (
              <span key={index} className="block">{email}</span>
            ))}
          </p>
        )}
        {scanData?.dnsServers && (
          <p>
            <strong>DnsServers:</strong> {scanData.dnsServers.map((dns, index) => (
              <span key={index} className="block">{dns}</span>
            ))}
          </p>
        )}
      </div>
    )}
    </div>
  );
};

export default ScanResultCard;

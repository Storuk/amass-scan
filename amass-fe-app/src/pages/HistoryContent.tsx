import { useState } from "react";
import useGetScanHistory from "../hooks/useGetScanHistory";
import ScanResultCard from "../components/ScanResultCard";

const HistoryContent = () => {
  const { data: scansList } = useGetScanHistory();
  const [visibleRows, setVisibleRows] = useState<number>(6);

  const handleShowMore = () => {
    setVisibleRows((prev) => prev + 6);
  };

  return (
    <div className="w-full lg:w-3/5 max-w-7xl mx-auto p-4">
      <h1 className="text-3xl font-semibold mb-6 text-gray-800">
        Scan History
      </h1>

      <div className="flex flex-wrap gap-2">
        {" "}
        {scansList
          ?.slice(0, visibleRows)
          .map((scan, index) => (
            <ScanResultCard key={index} scanResponse={scan} />
          ))}
      </div>

      {scansList && visibleRows < scansList.length && (
        <div className="mt-4 text-center">
          <button
            onClick={handleShowMore}
            className="px-4 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600 focus:outline-none"
          >
            Show More
          </button>
        </div>
      )}
    </div>
  );
};

export default HistoryContent;

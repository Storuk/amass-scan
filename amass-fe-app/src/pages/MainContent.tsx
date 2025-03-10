import { useEffect, useState } from "react";
import ScanModalFormInputs from "../components/ScanModalForm";
import useMakeScan from "../hooks/useMakeScan";
import ScanResultCard from "../components/ScanResultCard";

const MainContent = () => {
  const [openScanModalForm, setOpenScanModalForm] = useState<boolean>(false);
  const [domain, setDomain] = useState<string | undefined>(undefined);

  const { data: makeScanData, mutate } = useMakeScan();

  useEffect(() => {
    if(domain){
      mutate(domain);
    }
  }, [domain])

  const changeOpenScanModalForm = () => {
    setOpenScanModalForm((prev) => !prev);
  };

  const closeModalOnOutsideClick = (
    event: React.MouseEvent<HTMLDivElement>
  ) => {
    event.stopPropagation(); // Stop event bubbling up
    setOpenScanModalForm(false);
  };

  return (
    <div className="w-full lg:w-3/5 mx-auto p-4">
      {/* Button to open modal */}
      <div className="flex justify-end">
        <button
          onClick={changeOpenScanModalForm}
          className="bg-blue-500 text-white py-2 px-4 rounded-lg hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-400 mr-4"
        >
          Add Scan
        </button>
      </div>

      {/* Modal */}
      {openScanModalForm && (
        <div
          className="fixed inset-0 flex items-center justify-center bg-black/50 backdrop-blur-md z-50"
          onClick={closeModalOnOutsideClick}
        >
          <div
            onClick={(e) => e.stopPropagation()}
            className=" p-8 rounded-lg w-full max-w-lg relative"
          >
            <ScanModalFormInputs
              onSubmitClose={changeOpenScanModalForm}
              setDomain={setDomain}
            />
          </div>
        </div>
      )}

      {/* Scan Result Card */}
      {makeScanData && (
        <div className="mt-6">
          {" "}
          {/* Adds space from the top */}
          <ScanResultCard scanResponse={makeScanData} />
        </div>
      )}
    </div>
  );
};

export default MainContent;

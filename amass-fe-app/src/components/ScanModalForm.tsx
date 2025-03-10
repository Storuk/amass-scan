import React from "react";
import { SubmitHandler, useForm } from "react-hook-form";

type ScanModalFormInputs = {
  domainName: string;
};

interface ScanModalFormProps {
  onSubmitClose: () => void;
  setDomain: (value: string) => void;
}

const ScanModalForm: React.FC<ScanModalFormProps> = ({
  onSubmitClose,
  setDomain,
}) => {
  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm<ScanModalFormInputs>();

  const onSubmit: SubmitHandler<ScanModalFormInputs> = (data) => {
    console.log(data);
    reset();
    setDomain(data.domainName);
    onSubmitClose();
  };

  return (
    <form
      onSubmit={handleSubmit(onSubmit)}
      className="bg-white p-8 py-10 rounded-lg shadow-lg w-full max-w-lg relative"
    >
      {/* Close Button */}
      <button
        type="button"
        onClick={onSubmitClose}
        className="absolute top-2 right-4 text-gray-500 hover:text-gray-700 text-xl"
      >
        ×
      </button>

      {/* Form Title */}
      <h2 className="text-xl font-semibold text-gray-800 mb-4">Start a Scan</h2>

      {/* Input Field */}
      <div className="mb-4">
        <label
          htmlFor="domainName"
          className="block text-sm font-medium text-gray-700"
        >
          Domain Name
        </label>
        <input
          {...register("domainName", { required: "Domain is required" })}
          id="domainName"
          type="text"
          placeholder="Enter domain..."
          className="w-full p-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-400 focus:outline-none"
        />
        {errors.domainName && (
          <p className="text-sm text-red-500 mt-1">
            {errors.domainName.message}
          </p>
        )}
      </div>

      {/* Submit Button */}
      <button
        type="submit"
        className="w-full bg-blue-500 text-white py-2 px-4 rounded-lg hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-400"
      >
        Scan
      </button>
    </form>
  );
};

export default ScanModalForm;

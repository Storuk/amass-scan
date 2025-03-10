import { Link } from "react-router-dom";

const Header: React.FC = () => {
  return (
    <header className="bg-gray-100 text-gray-800 p-4 shadow-sm">
      <nav className="mx-auto max-w-7xl">
        <ul className="flex space-x-6 w-full lg:w-2/3 justify-start mx-auto">
          <li>
            <Link to="/" className="hover:underline">
              Main
            </Link>
          </li>
          <li>
            <Link to="/history" className="hover:underline">
              History
            </Link>
          </li>
        </ul>
      </nav>
    </header>
  );
};

export default Header;

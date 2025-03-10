import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import MainContent from "./pages/MainContent";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import HistoryContent from "./pages/HistoryContent";
import Header from "./components/Header";

function App() {
  const queryClient = new QueryClient();

  return (
    <>
      <QueryClientProvider client={queryClient}>
        <Router>
          <Header />
          <div className="container mx-auto mt-8">
            <Routes>
              <Route path="/" element={<MainContent />} />
              <Route path="/history" element={<HistoryContent />} />
            </Routes>
          </div>
        </Router>
      </QueryClientProvider>
    </>
  );
}

export default App;

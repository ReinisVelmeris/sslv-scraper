import React from "react";
import { Routes, Route } from "react-router-dom";
import LandingPage from "./scenes/LandingPage";
import Putter from "./scenes/Putter";

function App() {
  return (
    <div className="App">
      <header className="App-header">
      </header>
      <Routes>
        <Route path="/" element={<LandingPage />} />
        <Route path="/putter" element={<Putter />} />
      </Routes>
    </div>
  );
}

export default App;

import { BrowserRouter, Routes, Route } from "react-router-dom";
import Home from "./Home"
import Template from './template/Template';

function App() {
  return (
    <BrowserRouter>
      <Template>
        <Routes>
          <Route path="/" element={<Home />} />
        </Routes>
      </Template>
    </BrowserRouter>
  )
}

export default App

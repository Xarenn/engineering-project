import './App.css';
import NavBarComponent from "./components/NavBarComponent";
import { Routes, Route, Link } from "react-router-dom";
import LoginComponent from "./components/LoginComponent";
import RegisterComponent from "./components/RegisterComponent";
import Logout from "./components/LogoutComponent";
import MessageBoxComponent from "./components/MessageBoxComponent";
import axios from "axios";

function Home() {
    return (
        <>
            <main style={{marginLeft: '20px'}}>
                <h2>Witaj na aplikacji RealTimeChat</h2>
                <p>Diabeł tkwi w szczegółach - cała magia kryję się na serwerze;)</p>
                <h2>Who am I?</h2>
                <p>
                    Entuzjast systemów low latency, real time oraz miłośnik programowania w językach wysokiego poziomu (Java, Python, JS)
                </p>
            </main>
        </>
    );
}

function About() {
    return (
        <>
            <main>
                <h2>Who am I?</h2>
                <p>
                    Entuzjast systemów low latency, real time oraz miłośnik programowania w językach
                </p>
            </main>
            <nav>
                <Link to="/">Home</Link>
            </nav>
        </>
    );
}

function App() {
    return <div>
        <NavBarComponent/>
        <Routes handler={App}>
            <Route path="/" element={<Home />} />
            <Route path="/logout" element={<Logout />} />
            <Route path="about" element={<About />} />
            <Route path="login" element={<LoginComponent />} />
            <Route path="register" element={<RegisterComponent />} />
            <Route path="messages" element={<MessageBoxComponent />} />
        </Routes>
    </div>;
}

export default App;

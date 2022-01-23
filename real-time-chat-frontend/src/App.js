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
            <main>
                <h2>Welcome to the homepage! HIU</h2>
                <p>You can do this, I believe in you.</p>
            </main>
        </>
    );
}

function About() {
    return (
        <>
            <main>
                <h2>Who are we?</h2>
                <p>
                    That feels like an existential question, don't you
                    think?
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

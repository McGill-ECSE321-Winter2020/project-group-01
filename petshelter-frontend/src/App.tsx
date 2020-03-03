import React from 'react';
import logo from './default.png';
import profile from './profile.png';
import './App.css';
import Navbar from 'react-bootstrap/Navbar'
import Nav from 'react-bootstrap/Nav'

// The landing page of the website.
function App() {
    return (
    <div className="App">
        <Navbar bg="dark">
            <Navbar.Brand href="#home">
                <img
                    src={profile}
                    width="60"
                    height="60"
                    className="d-inline-block align-top"
                    alt="logo"
                />
            </Navbar.Brand>
            <Navbar.Collapse className="justify-content-end">
                <Nav.Link href="#home">Home</Nav.Link>
                <Nav.Link href="#register">Register</Nav.Link>
                <Nav.Link href="#login">Login</Nav.Link>
                <Nav.Link href="#donate">Donate</Nav.Link>
            </Navbar.Collapse>
        </Navbar>
        <header className="App-header">
            <img src={logo} className="App-logo" alt="logo" />
        </header>
    </div>
  );
}

export default App;

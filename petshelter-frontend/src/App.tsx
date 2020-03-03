import React from 'react';
import logo from './default.png';
import profile from './profile.png';
import './App.css';
import Navbar from 'react-bootstrap/Navbar'
import Nav from 'react-bootstrap/Nav'
import {MdPersonAdd} from 'react-icons/md';
import {MdPersonPin} from 'react-icons/md';
import {MdMonetizationOn} from 'react-icons/md';
import { IconContext } from "react-icons";

// The landing page of the website.
function App() {
    return (
    <div className="App">
        <Navbar bg="dark">
            <Navbar.Brand href="#home">
                <span>
                <img
                    src={profile}
                    width="60"
                    height="60"
                    className="d-inline-block align-top"
                    alt="logo"
                /> Pet Pawlace
                </span>
            </Navbar.Brand>
            <Navbar.Collapse className="justify-content-end">
                <IconContext.Provider value={{ color: "white", className: "global-class-name", size:"2em" }}>
                <Nav.Link href="#register">
                    <MdPersonAdd></MdPersonAdd>
                    <div className="bg">
                        Register
                    </div>
                </Nav.Link>
                <Nav.Link href="#login">
                    <MdPersonPin></MdPersonPin>
                    <div className="bg">
                        Login
                    </div>
                </Nav.Link>
                <Nav.Link href="#donate">
                    <MdMonetizationOn></MdMonetizationOn>
                    <div className="bg">
                        Donate
                    </div>
                </Nav.Link>
                </IconContext.Provider>
            </Navbar.Collapse>
        </Navbar>
        <header className="App-header">
            <img src={logo} className="App-logo" alt="logo" />
        </header>
    </div>
  );
}

export default App;

import React, {Component} from 'react';
import logo from './default.png';
import profile from './profile.png';
import './App.css';
import Navbar from 'react-bootstrap/Navbar'
import Nav from 'react-bootstrap/Nav'
import {MdPersonAdd} from 'react-icons/md';
import {MdPersonPin} from 'react-icons/md';
import {MdMonetizationOn} from 'react-icons/md';
import {MdAssignment} from 'react-icons/md';
import { IconContext } from "react-icons";
import SignUp from './Components/SignUp'
import Register from './Components/Register'
import Application from './Components/Application'


// The landing page of the website.
interface IProps {
}

interface IState {
    donateRegisterLoginHome?: string;
}
class App extends Component<IProps, IState>{
    constructor(props:IProps) {
        super(props);
        this.state = {
            donateRegisterLoginHome: 'Home'
        };
    }
    render() {
        return (
            <div className="App">
                <Navbar bg="dark" expand={true} collapseOnSelect={false} style={{maxHeight:"10vh"}}>
                    <Navbar.Brand href="" onClick={() => this.changeState('Home')}>
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
                            <Nav.Link className="a2" onClick={() => this.changeState('Register')}>
                                <MdPersonAdd/>
                                <div className="bg">
                                    Register
                                </div>
                            </Nav.Link>
                            <Nav.Link className="a2" onClick={() => this.changeState('Login')}>
                                <MdPersonPin/>
                                <div className="bg">
                                    Login
                                </div>
                            </Nav.Link>
                            <Nav.Link className="a2" onClick={() => this.changeState('Donate')}>
                                <MdMonetizationOn/>
                                <div className="bg">
                                    Donate
                                </div>
                            </Nav.Link>
                            <Nav.Link className="a2" onClick={() => this.changeState('Application')}>
                                <MdAssignment/>
                                <div className="bg">
                                    Application
                                </div>
                            </Nav.Link>
                        </IconContext.Provider>
                    </Navbar.Collapse>
                </Navbar>
                <header className="App-header">
                    {this.state.donateRegisterLoginHome === 'Home' && <img src={logo} className="App-logo" alt="logo"/>}
                    {this.state.donateRegisterLoginHome === 'Register' && <Register/>}
                    {this.state.donateRegisterLoginHome === 'Login' && <SignUp/>}
                    {this.state.donateRegisterLoginHome === 'Donate' && <img src={logo} className="App-logo" alt="logo" />}
                    {this.state.donateRegisterLoginHome === 'Application' && <Application/>}

                </header>

            </div>
        );
    }

    changeState(state: string){
        this.setState({
            donateRegisterLoginHome: state
        });
    }
}

export default App;

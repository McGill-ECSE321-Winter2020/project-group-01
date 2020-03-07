import React, {Component} from 'react';
import logo from './default.png';
import profile from './profile.png';
import './App.css';
import Navbar from 'react-bootstrap/Navbar'
import Nav from 'react-bootstrap/Nav'
import {MdMonetizationOn, MdPersonAdd, MdPersonPin} from 'react-icons/md';
import {IconContext} from "react-icons";
import SignUp from './Components/SignUp'
import Register from './Components/Register'
import Donate from "./Components/Donate";
import DonationCard from "./Components/DonationCard";


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
                        </IconContext.Provider>
                    </Navbar.Collapse>
                </Navbar>
                <header className="App-header">
                    {this.state.donateRegisterLoginHome === 'Home' && <img src={logo} className="App-logo" alt="logo"/>}
                    {this.state.donateRegisterLoginHome === 'Register' && <Register/>}
                    {this.state.donateRegisterLoginHome === 'Login' && <SignUp/>}
                    {this.state.donateRegisterLoginHome === 'Donate' && <Donate isHome={true}/>}
                </header>

            </div>
        );
    }
//<Donate isHome={true}/>
//<DonationCard amount={11} date={"today"} email={"email"} username={"this is my username"}/>
    changeState(state: string){
        this.setState({
            donateRegisterLoginHome: state
        });
    }
}

export default App;

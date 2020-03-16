import React, {Component} from 'react';
import logo from './default.png';
import profile from './profile.png';
import './App.css';
import Navbar from 'react-bootstrap/Navbar'
import Nav from 'react-bootstrap/Nav'
import {MdMonetizationOn, MdPersonAdd, MdPersonPin, MdPets, MdShoppingCart, MdForum, MdFace} from 'react-icons/md';
import {IconContext} from "react-icons";
import Register from './Components/Register'
import SignIn from "./Components/SignIn";
import Donate from "./Components/Donate";
import UserInformation from "./CurrentUserInformation";
import DashBoard from "./Components/DashBoard";


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
        this.handler = this.handler.bind(this)
        this.isTheUserLoggedIn();
    }
    render() {
        return (
            <div>
                <div className="App" id="home">
                    <Navbar bg="dark" expand={true} collapseOnSelect={false} style={{maxHeight: "10vh"}}>
                        <Navbar.Brand href="" onClick={() => this.changeState('Home')}>
                        <span>
                            <img
                                src={profile}
                                width="70"
                                height="70"
                                className="d-inline-block align-middle"
                                alt="logo"
                            /> Pawlace
                        </span>
                    </Navbar.Brand>
                        {!UserInformation.loggedIn &&
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
                    </Navbar.Collapse>}
                        {UserInformation.loggedIn &&
                        <Navbar.Collapse className="justify-content-end">
                            <IconContext.Provider value={{ color: "white", className: "global-class-name", size:"2em" }}>
                                <Nav.Link className="a2" onClick={() => this.changeState('Forums')}>
                                    <MdForum/>
                                    <div className="bg">
                                        Forum
                                    </div>
                                </Nav.Link>
                                <Nav.Link className="a2" onClick={() => this.changeState('Advertisements')}>
                                    <MdShoppingCart/>
                                    <div className="bg">
                                        Advertisements
                                    </div>
                                </Nav.Link>
                                <Nav.Link className="a2" onClick={() => this.changeState('Pets')}>
                                    <MdPets/>
                                    <div className="bg">
                                        Pets
                                    </div>
                                </Nav.Link>
                                <Nav.Link className="a2" onClick={() => this.changeState('Profile')}>
                                    <MdFace/>
                                    <div className="bg">
                                        Profile
                                    </div>
                                </Nav.Link>
                                <Nav.Link className="a2" onClick={() => this.changeState('Donate')}>
                                    <MdMonetizationOn/>
                                    <div className="bg">
                                        Donate
                                    </div>
                                </Nav.Link>
                            </IconContext.Provider>
                        </Navbar.Collapse>}
                    </Navbar>
                    <header className="App-header">
                        {this.state.donateRegisterLoginHome === 'Home' &&
                        <img src={logo} className="App-logo" alt="logo"/>}
                        {this.state.donateRegisterLoginHome === 'Register' && <Register/>}
                        {this.state.donateRegisterLoginHome === 'Login' && !UserInformation.loggedIn && <SignIn userInfo={UserInformation} handler={this.handler}/>}
                        {this.state.donateRegisterLoginHome === 'Donate' && <Donate isHome={true}/>}
                        {this.state.donateRegisterLoginHome === 'Login' && UserInformation.loggedIn && <DashBoard/>}
                    </header>
                </div>
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

    //updates the user properties
    handler(userInfo: any){
        UserInformation.token=userInfo.token;
        UserInformation.loggedIn=userInfo.loggedIn;
        UserInformation.isAdmin=userInfo.isAdmin;
        this.setState({donateRegisterLoginHome:""});
        this.render();
        console.log(UserInformation)
    }

    isTheUserLoggedIn(){
        if(UserInformation.token!==""){
            //make backend call with token
            fetch("http://petshelter-backend.herokuapp.com/api/user/register", {
                method: 'post',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    token: UserInformation.token
                })
            }).then((response) => {
                if (response.status === 200) {
                    UserInformation.loggedIn=true;
                } else {
                    UserInformation.token="";
                    UserInformation.isAdmin=false;
                    UserInformation.loggedIn=false;
                }
            })
        }
        else{
            UserInformation.token="";
            UserInformation.loggedIn=false;
        }
    }
}

export default App;

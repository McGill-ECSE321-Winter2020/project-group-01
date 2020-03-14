import React, {Component} from 'react';
import Navbar from "react-bootstrap/Navbar";
import {IconContext} from "react-icons";
import Nav from "react-bootstrap/Nav";
import {Search} from "@material-ui/icons";
import Application from "./Application";

interface IProps {

}

interface IState {
    email: string,
    token: string,
    username: string,
    dashBoardStates?: string
    //maybe the other dashboards / forurm
}

/*
 *
 */
class DashBoard extends Component<IProps, IState> {
    constructor(props:IProps) {
        super(props);
        this.state = {
            email: '',
            token: '',
            username:''
        }
    }

    render(){
        return (
            <div className="App">
                <Navbar bg="dark" expand={true} collapseOnSelect={false} style={{maxHeight: "10vh"}}>
                    <Navbar.Brand href="" onClick={() => this.changeState('Home')}>
                        <span>
                            Home
                        </span>
                    </Navbar.Brand>
                    <Navbar.Collapse className="justify-content-end">
                        <IconContext.Provider value={{color: "white", className: "global-class-name", size: "2em"}}>

                            <Nav.Link className="a2" onClick={() => this.changeState('Profile')}>
                                <div className="bg">
                                    My Profile
                                </div>
                            </Nav.Link>

                            <Nav.Link className="a2" onClick={() => this.changeState('MyPets')}>
                                <div className="bg">
                                    My Pets
                                </div>
                            </Nav.Link>

                            <Nav.Link className="a2" onClick={() => this.changeState('MyAds')}>
                                <div className="bg">
                                    My Advertisement
                                </div>
                            </Nav.Link>

                            <Nav.Link className="a2" onClick={() => this.changeState('AdoptPet')}>

                                <div className="bg">
                                    Adopt a Pet
                                </div>
                            </Nav.Link>

                            <Nav.Link className="a2" onClick={() => this.changeState('CreateAd')}>
                                <div className="bg">
                                    Create Advertisement
                                </div>
                            </Nav.Link>

                            <Nav.Link className="a2" onClick={() => this.changeState('Forum')}>
                                <div className="bg">
                                    Forum
                                </div>
                            </Nav.Link>

                            <Nav.Link className="a2" onClick={() => this.changeState('Search')}>
                                <div className="bg">
                                    Search
                                </div>
                            </Nav.Link>

                        </IconContext.Provider>
                    </Navbar.Collapse>
                </Navbar>
                <h1>Welcome {this.state.username} !</h1>
                <header className="App-header">
                    {this.state.dashBoardStates === 'Home'}
                    {this.state.dashBoardStates === 'AdoptPet' && <Application/>}
                    {this.state.dashBoardStates === 'CreateAd'}
                    {this.state.dashBoardStates === 'Forum'}
                    {this.state.dashBoardStates === 'Search' && <Search/>}
                    {this.state.dashBoardStates === 'Profile'}
                    {this.state.dashBoardStates === 'MyPets'}
                    {this.state.dashBoardStates === 'MyAds'}
                </header>
            </div>
        );
    }

    changeState(state: string) {
        this.setState({
            dashBoardStates: state
        });
    }
}

export default DashBoard;
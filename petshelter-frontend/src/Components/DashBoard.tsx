import React, {Component} from 'react';
import Navbar from "react-bootstrap/Navbar";
import {IconContext} from "react-icons";
import Nav from "react-bootstrap/Nav";

interface IProps {

}

interface IState {
    email: string,
    token: string,
    username: string,
    forumPetAdoption?: string
    //maybe the other dashboards / forurm
}

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
                    {this.state.forumPetAdoption === 'Home'}
                    {this.state.forumPetAdoption === 'AdoptPet'}
                    {this.state.forumPetAdoption === 'CreateAd'}
                    {this.state.forumPetAdoption === 'Forum'}
                    {this.state.forumPetAdoption === 'Search'}
                </header>
            </div>
        );
    }

    changeState(state: string) {
        this.setState({
            forumPetAdoption: state
        });
    }
}

export default DashBoard;
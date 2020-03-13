import React, {Component} from 'react';
import {Container} from "@material-ui/core";
import Button from "@material-ui/core/Button";
import Typography from "@material-ui/core/Typography";
import App from "../App";

interface IProps {
    isHome: boolean,
    username: string
}

interface IState {
    isDone: boolean
}

class ThankYou extends Component<IProps, IState> {
    constructor(props: IProps) {
        super(props);
        this.state = {
            isDone: false
        };
        this.doneDonation = this.doneDonation.bind(this);

    }

    render() {

        const renderLandingPage = () => {
            if (this.props.isHome)
                return (
                    <App/>
                )
        };


        return (
            <div>
                {!this.state.isDone && <Container component="main" maxWidth="xs">
                    <Typography component="h1" variant="h5" style={{color: "black"}}>
                        Thank you {this.props.username} !
                    </Typography>
                    <Button
                        type="submit"
                        fullWidth
                        variant="contained"
                        color="primary"
                        style={{marginTop: "5%",}}
                        onClick={() => this.doneDonation}
                    >
                        Go Back!
                    </Button>
                </Container>}
                {this.state.isDone && renderLandingPage()}
            </div>

        );
    }

    changeState(state: boolean) {
        this.setState({
            isDone: state
        })
    }

    doneDonation(event) {
        console.log("122222");
        console.log(this.state);
        /*
        depending on home page donation or logged in donation, we can return the user
        to home page or to their dashboard
         */
        event.preventDefault();
        this.changeState(true);
    }

}

export default ThankYou;
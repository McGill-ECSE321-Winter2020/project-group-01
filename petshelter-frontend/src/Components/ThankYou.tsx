import React, {Component} from 'react';
import {Container} from "@material-ui/core";
import Button from "@material-ui/core/Button";

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
        return (
            <Container>
                Thank you {this.props.username};
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
            </Container>
        );
    }

    changeState(state: boolean) {
        this.setState({
            isDone: state
        })
    }

    doneDonation(event) {
        /*
        depending on home page donation or logged in donation, we can return the user
        to home page or to their dashboard
         */

        this.changeState(true);
    }

}

export default ThankYou;
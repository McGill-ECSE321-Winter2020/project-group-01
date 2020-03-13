import React, {Component} from 'react';
import {Container} from "@material-ui/core";
import Button from "@material-ui/core/Button";
import Typography from "@material-ui/core/Typography";
import App from "../App";
import CssBaseline from "@material-ui/core/CssBaseline";
import Avatar from "@material-ui/core/Avatar";
import LockOutlinedIcon from "@material-ui/icons/LockOutlined";
import Box from "@material-ui/core/Box";

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
        //takes user back to home page or the dashboard
        const renderLandingPage = () => {
            if (!this.props.isHome)
                return (
                    ""
                );
            else
                return (<App/>);
        };


        return (
            <div>
                {!this.state.isDone && <Container component="main" maxWidth="xs">
                    <CssBaseline/>
                    <div style={{
                        marginTop: "10%",
                        display: 'flex',
                        flexDirection: 'column',
                        alignItems: 'center'
                    }}>
                        <Avatar style={{
                            margin: "2%",
                            backgroundColor: "#2BE0A2",
                        }}>
                            <LockOutlinedIcon/>
                        </Avatar>
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
                    </div>
                    <Box mt={5}>
                    </Box>
                </Container>
                }
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
        console.log(this.state);


        event.preventDefault();
        this.changeState(true);
    }

}

export default ThankYou;
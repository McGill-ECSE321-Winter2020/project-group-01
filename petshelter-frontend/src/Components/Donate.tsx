import React, {Component} from 'react';
import Container from '@material-ui/core/Container';
import {Grid, TextField} from "@material-ui/core";
import CssBaseline from "@material-ui/core/CssBaseline";
import Avatar from "@material-ui/core/Avatar";
import LockOutlinedIcon from "@material-ui/icons/LockOutlined";
import Typography from "@material-ui/core/Typography";
import Button from "@material-ui/core/Button";
import Box from "@material-ui/core/Box";

interface IProps {
    isHome:boolean
}

interface IState {
    email: string,
    amount: number,
    token: string
}

/*
the button should lead to a thank you page where an email confirming the amount will be sent to the user
if there is a email. In the backend

also, add a form if if statement that hides the email html if we are logged in
if the person is logged in, we can fetch its token and get the user's info
else, we keep the token as '' and we can send an email if the user wants to
 */

class Donate extends Component<IProps, IState> {
    constructor(props: IProps) {
        super(props);
        this.state = {
            amount: 0,
            email: '',
            token: ''
        };
        this.handleAmount = this.handleAmount.bind(this);
        this.handleEmail = this.handleEmail.bind(this);
        this.handleToken = this.handleToken.bind(this);
    }

    handleAmount(event) {
        this.setState({
            amount: event.target.value
        });
    }

    handleEmail(event) {
        this.setState({
            email: event.target.value
        })
    }

    handleToken(event) {
        this.setState({
            token: event.target.value
        })
    }

    render() {

        const renderEmail = () =>{
            if(this.props.isHome){
                return(
                    <Grid item xs={12}>
                        <TextField
                            variant="outlined"
                            fullWidth
                            id="email"
                            label="Email Address (Optional)"
                            name="email"
                            autoComplete="email"
                        />
                    </Grid>
                )
            }
        };

        return (
            <Container component="main" maxWidth="xs">
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
                        Donation
                    </Typography>
                    <form style={{
                        width: '100%',
                        marginTop: "2%"
                    }} noValidate>
                        <Grid container spacing={2}>

                            {renderEmail()}

                            <Grid item xs={12}>
                                <TextField
                                    variant="outlined"
                                    fullWidth
                                    name="amount"
                                    label="amount"
                                    type="amount"
                                    id="amount"
                                />
                            </Grid>
                        </Grid>
                        <Button
                            type="submit"
                            fullWidth
                            variant="contained"
                            color="primary"
                            style={{marginTop: "5%",}}
                            onClick={() => this.submitDonation}
                        >
                            Donate!
                        </Button>
                    </form>
                </div>
                <Box mt={5}>
                </Box>
            </Container>
        );
    };

    submitDonation(event) {
        fetch("http://petshelter-backend.herokuapp.com/api/donation/", {
            method: "post",
            headers: {'Content-Type': 'application/json',},
            body: JSON.stringify({
                email: this.state.email,
                amount: this.state.amount,
                token: this.state.token
            })
        }).then(function (response) {
            if (response.status == 200) {
                return response;
            }
            throw new Error('Network response was not ok.');
        }).then(function (data) {
            console.log(data);
        }).catch(function (error) {
            console.log('There has been a problem with your fetch operation: ' + error);
        });
        event.preventDefault();
    }
}

// @ts-ignore
export default Donate;
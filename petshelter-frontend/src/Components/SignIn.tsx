import React, {Component} from 'react';
import Avatar from '@material-ui/core/Avatar';
import Button from '@material-ui/core/Button';
import CssBaseline from '@material-ui/core/CssBaseline';
import TextField from '@material-ui/core/TextField';
import Link from '@material-ui/core/Link';
import Grid from '@material-ui/core/Grid';
import Box from '@material-ui/core/Box';
import LockOutlinedIcon from '@material-ui/icons/LockOutlined';
import CheckCircleIcon from '@material-ui/icons/LockOutlined';
import Typography from '@material-ui/core/Typography';
import Container from '@material-ui/core/Container';
import DashBoard from "./DashBoard";

interface IProps {
}

interface IState {
    password: string,
    username: string,
    loginOrReset?: string,
    hasError: boolean,
    error: string
}

class SignIn extends Component<IProps, IState> {
    constructor(props: IProps) {
        super(props);
        this.state = {
            password: '',
            username: '',
            loginOrReset: 'Login',
            hasError: false,
            error: ''
        };
        this.handleUsername = this.handleUsername.bind(this);
        this.handlePassword = this.handlePassword.bind(this);
        this.submitForm = this.submitForm.bind(this);
    }


    handlePassword(event) {
        this.setState({password: event.target.value});
    }

    handleUsername(event) {
        this.setState({username: event.target.value});
    }

    render() {
        return (
            <Container component="main" maxWidth="xs">
                <CssBaseline/>
                {this.state.loginOrReset === 'Login' &&
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
                        Sign in
                    </Typography>
                    <form style={{
                        width: '100%',
                        marginTop: "2%"
                    }} noValidate onSubmit={this.submitForm}>
                        <TextField
                            variant="outlined"
                            margin="normal"
                            fullWidth
                            id="username"
                            label="Username"
                            name="username"
                            autoComplete="username"
                            autoFocus
                            onChange={this.handleUsername}
                        />
                        <TextField
                            variant="outlined"
                            margin="normal"
                            fullWidth
                            name="password"
                            label="Password"
                            type="password"
                            id="password"
                            autoComplete="current-password"
                            onChange={this.handlePassword}
                        />
                        <Button
                            type="submit"
                            fullWidth
                            variant="contained"
                            color="primary"
                            style={{marginTop: "5%", marginBottom: "3%",}}
                        >
                            Sign In
                        </Button>
                        <Grid container>
                            <Grid item xs>
                                <Link href="#" variant="body2" onClick={this.submitReset}>
                                    Forgot password?
                                </Link>
                            </Grid>
                        </Grid>
                    </form>
                </div>}
                {this.state.loginOrReset === 'Reset' &&
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
                        <CheckCircleIcon/>
                    </Avatar>
                    <Typography component="h1" variant="h5" style={{color: "black"}}>
                        Password reset! A new password was sent to your email address.
                        Refresh the page when ready to login.
                    </Typography>
                </div>
                }
                {this.state.hasError && <p style={{color: "red", fontSize: "0.7em", fontWeight: "bold"}}>
                    {this.state.error}
                </p>}

                {this.state.loginOrReset === "LoggedIn" && <DashBoard/>}
                <Box mt={8}>
                </Box>
            </Container>
        );
    };

    submitForm(event) {
        fetch("http://petshelter-backend.herokuapp.com/api/user/login", {
            method: 'post',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                password: this.state.password,
                username: this.state.username,
            })
        }).then((response) => {
            if (response.status === 200) {
                console.log(response);
                this.setState({hasError: false});
                this.setState({error: ''});
                this.setState({loginOrReset: 'Done'});
                return response.text();
            } else {
                this.setState({hasError: true});

                console.log(this.state.hasError);
                return response.text();
            }
        }).then((data) => {
            if (this.state.hasError) {
                this.setState({error: data});
            }
            console.log(data);
        }).catch(function (error) {
            console.log('There has been a problem with your fetch operation: ' + error);
        });
        event.preventDefault();
        this.setState({
            loginOrReset: "LoggedIn"
        });
    }

    submitReset() {
        if (this.state.username === '') {
            this.setState({hasError: true});
            this.setState({error: 'Enter your username first.'});
        } else {
            fetch("http://petshelter-backend.herokuapp.com/api/user/resetPassword", {
                method: 'post',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    username: this.state.username,
                })
            }).then((response) => {
                if (response.status === 200) {
                    console.log(response);
                    this.setState({hasError: false});
                    this.setState({error: ''});
                    this.setState({loginOrReset: 'Reset'});
                    return response.text();
                } else {
                    this.setState({hasError: true});

                    console.log(this.state.hasError);
                    return response.text();
                }
            }).then((data) => {
                if (this.state.hasError) {
                    this.setState({error: data});
                }
                console.log(data);
            }).catch(function (error) {
                console.log('There has been a problem with your fetch operation: ' + error);
            });
        }
    }

}

export default SignIn;
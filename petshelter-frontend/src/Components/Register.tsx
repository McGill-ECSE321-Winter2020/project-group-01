import React, {Component} from 'react';
import Avatar from '@material-ui/core/Avatar';
import Button from '@material-ui/core/Button';
import CssBaseline from '@material-ui/core/CssBaseline';
import TextField from '@material-ui/core/TextField';
import Grid from '@material-ui/core/Grid';
import Box from '@material-ui/core/Box';
import LockOutlinedIcon from '@material-ui/icons/LockOutlined';
import CheckCircleIcon from '@material-ui/icons/LockOutlined';
import Typography from '@material-ui/core/Typography';
import Container from '@material-ui/core/Container';

interface IProps {
}

interface IState {
    password: string,
    email: string,
    username: string,
    registerOrConfirm?: string,
    hasError: boolean,
    error: string
}
class Register extends Component<IProps, IState> {
    constructor(props: IProps) {
        super(props);
        this.state = {
            password: '',
            email: '',
            username: '',
            registerOrConfirm: 'Register',
            hasError: false,
            error: ''
        };
        this.handleUsername = this.handleUsername.bind(this);
        this.handleEmail = this.handleEmail.bind(this);
        this.handlePassword = this.handlePassword.bind(this);
        this.submitForm = this.submitForm.bind(this);
    }

    handleEmail(event) {
        this.setState({email: event.target.value});
    }
    handlePassword(event) {
        this.setState({password: event.target.value});
    }

    handleUsername(event) {
        this.setState({username: event.target.value});
    }


    render(){
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
                        {this.state.registerOrConfirm === 'Register' && <LockOutlinedIcon/>}
                        {this.state.registerOrConfirm === 'Confirm' && <CheckCircleIcon/>}
                    </Avatar>
                    {this.state.registerOrConfirm === 'Register' &&
                    <Typography component="h1" variant="h5" style={{color: "black"}}>
                        Sign up
                    </Typography>}
                    {this.state.registerOrConfirm === 'Confirm' &&
                    <Typography component="h1" variant="h5" style={{color: "black"}}>
                        Account created!
                        Verify your account by clicking the link that was sent by email.
                    </Typography>}
                    {this.state.registerOrConfirm === 'Register' &&
                    <form style={{
                        width: '100%',
                        marginTop: "2%"
                    }} noValidate onSubmit={this.submitForm}>
                        <Grid container spacing={2}>
                            <Grid item xs={12}>
                                <TextField
                                    variant="outlined"
                                    fullWidth
                                    id="username"
                                    label="Username"
                                    name="username"
                                    autoComplete="username"
                                    onChange={this.handleUsername}
                                    value={this.state.username}
                                />
                            </Grid>
                            <Grid item xs={12}>
                                <TextField
                                    variant="outlined"
                                    fullWidth
                                    id="email"
                                    label="Email Address"
                                    name="email"
                                    autoComplete="email"
                                    onChange={this.handleEmail}
                                    value={this.state.email}
                                />
                            </Grid>
                            <Grid item xs={12}>
                                <TextField
                                    variant="outlined"
                                    fullWidth
                                    name="password"
                                    label="Password"
                                    type="password"
                                    id="password"
                                    autoComplete="current-password"
                                    onChange={this.handlePassword}
                                    value={this.state.password}
                                />
                            </Grid>
                        </Grid>
                        <Button
                            type="submit"
                            fullWidth
                            variant="contained"
                            color="primary"
                            style={{marginTop: "5%", marginBottom: "5%"}}
                            onClick={() => this.submitForm}
                        >
                            Sign Up
                        </Button>
                        {this.state.hasError && <p style={{color: "red", fontSize: "0.7em", fontWeight: "bold"}}>
                            {this.state.error}
                        </p>}
                    </form>}
                </div>
                <Box mt={5}>
                </Box>
            </Container>
        );
    };

    submitForm(event) {
        fetch("http://petshelter-backend.herokuapp.com/api/user/register", {
            method: 'post',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                password: this.state.password,
                username: this.state.username,
                email: this.state.email,
                userType: 'USER',
            })
        }).then((response) => {
            if (response.status === 201) {
                console.log(response);
                this.setState({hasError: false});
                this.setState({error: ''});
                this.setState({registerOrConfirm: 'Confirm'});
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
        }).catch(function(error) {
            console.log('There has been a problem with your fetch operation: ' + error);
        });
        event.preventDefault();
    }
}

export default Register;
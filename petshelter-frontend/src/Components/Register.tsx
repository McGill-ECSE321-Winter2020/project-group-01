import React, {Component} from 'react';
import Avatar from '@material-ui/core/Avatar';
import Button from '@material-ui/core/Button';
import CssBaseline from '@material-ui/core/CssBaseline';
import TextField from '@material-ui/core/TextField';
import Grid from '@material-ui/core/Grid';
import Box from '@material-ui/core/Box';
import LockOutlinedIcon from '@material-ui/icons/LockOutlined';
import Typography from '@material-ui/core/Typography';
import Container from '@material-ui/core/Container';

interface IProps {
}

interface IState {
    password: string,
    email: string,
    username: string,
    registerOrConfirm?: string
}
class Register extends Component<IProps, IState> {
    constructor(props: IProps) {
        super(props);
        this.state = {
            password: '',
            email: '',
            username: '',
            registerOrConfirm: 'Register'
        };
    }

    render(){
        return (
            <Container component="main" maxWidth="xs">
                <CssBaseline/>
                <div style={{marginTop: "10%",
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center'}}>
                    <Avatar style={{margin: "2%",
                        backgroundColor: "#2BE0A2",}}>
                        <LockOutlinedIcon/>
                    </Avatar>
                    <Typography component="h1" variant="h5" style={{color: "black"}}>
                        Sign up
                    </Typography>
                    <form style={{width: '100%',
                        marginTop: "2%"}} noValidate onSubmit={this.submitForm}>
                        <Grid container spacing={2}>
                            <Grid item xs={12}>
                                <TextField
                                    variant="outlined"
                                    fullWidth
                                    id="username"
                                    label="Username"
                                    name="username"
                                    autoComplete="username"
                                    onChange={this.handleUsernameChange}
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
                                    onChange={this.handleEmailChange}
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
                                    onChange={this.handlePasswordChange}
                                />
                            </Grid>
                        </Grid>
                        <Button
                            type="submit"
                            fullWidth
                            variant="contained"
                            color="primary"
                            style={{marginTop: "5%",}}
                            onClick={() => this.submitForm}
                        >
                            Sign Up
                        </Button>
                    </form>
                </div>
                <Box mt={5}>
                </Box>
            </Container>
        );
    };
    changeState(state: string){
        this.setState({registerOrConfirm: state})
    }
    handleEmailChange(e){
        this.setState({email: e.target.value});
    }
    handlePasswordChange(e){
        this.setState({password: e.target.value});
    }
    handleUsernameChange(e){
        this.setState({username: e.target.value});
    }

    submitForm(event) {
        fetch("http://petshelter-backend.herokuapp.com/api/user/register",{
            method: 'post',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                password: this.state.password,
                username: this.state.username,
                email: this.state.email,
            })
        }).then(function(response){
            if(response.ok) {
                console.log(response);
                return response;
            }
            throw new Error('Network response was not ok.');
        }).then(function(data) {
            console.log(data);
        }).catch(function(error) {
            console.log('There has been a problem with your fetch operation: ' + error);
        });
        event.preventDefault();
        this.changeState('Confirm');
    }
}

export default Register;
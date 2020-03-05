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

class Register extends Component {

    loginConfirmOrReset: string = 'Login';

    render(){
        return (
            <Container component="main" maxWidth="xs">
                <CssBaseline/>
                <div style={{marginTop: 8,
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center'}}>
                    <Avatar style={{margin: 1,
                        backgroundColor: "#2BE0A2",}}>
                        <LockOutlinedIcon/>
                    </Avatar>
                    <Typography component="h1" variant="h5" style={{color: "black"}}>
                        Sign up
                    </Typography>
                    <form style={{width: '100%',
                        marginTop: 3}} noValidate>
                        <Grid container spacing={2}>
                            <Grid item xs={12}>
                                <TextField
                                    variant="outlined"
                                    fullWidth
                                    id="username"
                                    label="Username"
                                    name="username"
                                    autoComplete="username"
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
                                />
                            </Grid>
                        </Grid>
                        <Button
                            type="submit"
                            fullWidth
                            variant="contained"
                            color="primary"
                            style={{marginTop: 5,}}
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
    }
    changeState(state: string){
        this.loginConfirmOrReset= state
    }
    submitForm() {

    }
}
export default Register
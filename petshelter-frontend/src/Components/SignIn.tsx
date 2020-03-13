import React, {Component} from 'react';
import Avatar from '@material-ui/core/Avatar';
import Button from '@material-ui/core/Button';
import CssBaseline from '@material-ui/core/CssBaseline';
import TextField from '@material-ui/core/TextField';
import Link from '@material-ui/core/Link';
import Grid from '@material-ui/core/Grid';
import Box from '@material-ui/core/Box';
import LockOutlinedIcon from '@material-ui/icons/LockOutlined';
import Typography from '@material-ui/core/Typography';
import Container from '@material-ui/core/Container';


class SignIn extends Component{
    loginOrReset: string = 'Login';

    render(){
        return (
            <Container component="main" maxWidth="xs">
                <CssBaseline />
                {this.loginOrReset === 'Login' &&
                <div style={{marginTop: "10%",
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center'}}>
                    <Avatar style={{margin: "2%",
                        backgroundColor: "#2BE0A2",}}>
                        <LockOutlinedIcon/>
                    </Avatar>
                    <Typography component="h1" variant="h5" style={{color: "black"}}>
                        Sign in
                    </Typography>
                    <form style={{width: '100%',
                        marginTop: "2%"}} noValidate>
                        <TextField
                            variant="outlined"
                            margin="normal"
                            fullWidth
                            id="email"
                            label="Email Address"
                            name="email"
                            autoComplete="email"
                            autoFocus
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
                                <Link href="#" variant="body2">
                                    Forgot password?
                                </Link>
                            </Grid>
                        </Grid>
                    </form>
                </div> }
                {this.loginOrReset === 'Reset'}
                <Box mt={8}>
                </Box>
            </Container>
        )}
}

export default SignIn
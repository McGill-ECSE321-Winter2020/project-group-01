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
import AssignmentIcon from '@material-ui/icons/Assignment';
import Typography from '@material-ui/core/Typography';
import Container from '@material-ui/core/Container';

interface IProps {
}

interface IState {
    description: string,
    adTitle: string,
    loginOrReset?: string,
    hasError: boolean,
    error: string
}
class Application extends Component<IProps, IState>{
    constructor(props: IProps) {
        super(props);
        this.state = {
            description: '',
            adTitle: '',
            loginOrReset: 'Login',
            hasError: false,
            error: ''
        };
        this.handleAdTitle = this.handleAdTitle.bind(this);
        this.handleDescription = this.handleDescription.bind(this);
        this.submitForm = this.submitForm.bind(this);
    }

    handleDescription(event) {
        this.setState({description: event.target.value});
    }

    handleAdTitle(event) {
        this.setState({adTitle: event.target.value});
    }

    render(){
        return (
            <Container component="main" maxWidth="xs">
                <CssBaseline />
                {this.state.loginOrReset === 'Login' &&
                <div style={{marginTop: "10%",
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center'}}>
                    <Avatar style={{margin: "2%",
                        backgroundColor: "#2BE0A2",}}>
                        <AssignmentIcon/>
                    </Avatar>
                    <Typography component="h1" variant="h5" style={{color: "black"}}>
                        Create Application
                    </Typography>
                    <form style={{width: '100%',
                        marginTop: "2%"}} noValidate onSubmit={this.submitForm}>
                        <TextField
                            variant="outlined"
                            margin="normal"
                            fullWidth
                            id="adTitle"
                            label="Advertisement Title"
                            name="adTitle"
                            autoComplete="adTitle"
                            autoFocus
                            onChange={this.handleAdTitle}
                        />
                        <TextField
                            variant="outlined"
                            margin="normal"
                            fullWidth
                            name="description"
                            label="Description"
                            type="description"
                            id="description"
                            autoComplete="current-description"
                            onChange={this.handleDescription}
                        />
                        <Button
                            type="submit"
                            fullWidth
                            variant="contained"
                            color="primary"
                            style={{marginTop: "5%", marginBottom: "3%",}}
                        >
                            Create Application
                        </Button>
                        <Grid container>
                        </Grid>
                    </form>
                </div> }
                {this.state.loginOrReset === 'Reset'&&
                <div style={{marginTop: "10%",
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center'}}>
                    <Avatar style={{margin: "2%",
                        backgroundColor: "#2BE0A2",}}>
                        <CheckCircleIcon/>
                    </Avatar>
                </div>
                }
                {this.state.hasError && <p style={{color: "red", fontSize: "0.7em", fontWeight: "bold"}}>
                    {this.state.error}
                </p>}
                <Box mt={8}>
                </Box>
            </Container>
        );
    };

    submitForm(event) {
        fetch("http://petshelter-backend.herokuapp.com/api/user/login",{
            method: 'post',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                description: this.state.description,
                adTitle: this.state.adTitle,
            })
        }).then((response) =>{
            if(response.status===200) {
                console.log(response);
                this.setState({hasError: false});
                this.setState({error: ''});
                this.setState({loginOrReset: 'Done'});
                return response.text();
            }
            else{
                this.setState({hasError: true});

                console.log(this.state.hasError);
                return response.text();
            }
        }).then((data) => {
            if(this.state.hasError){
                this.setState({error: data});
            }
            console.log(data);
        }).catch(function(error) {
            console.log('There has been a problem with your fetch operation: ' + error);
        });
        event.preventDefault();
    }

    submitReset() {
        if (this.state.adTitle === '') {
            this.setState({hasError: true});
            this.setState({error: 'Enter your advertisement title first.'});
        } else {
            fetch("http://petshelter-backend.herokuapp.com/api/user/resetPassword", {
                method: 'post',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    adTitle: this.state.adTitle,
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

export default Application;
import React, {Component} from 'react';
import Avatar from '@material-ui/core/Avatar';
import Button from '@material-ui/core/Button';
import CssBaseline from '@material-ui/core/CssBaseline';
import TextField from '@material-ui/core/TextField';
import Grid from '@material-ui/core/Grid';
import Box from '@material-ui/core/Box';
import Typography from '@material-ui/core/Typography';
import Container from '@material-ui/core/Container';
import datepicker from '@material-ui/pickers';
import PetsIcon from '@material-ui/icons/Pets';


interface IProps {
}

interface IState {

    name: string,
    dateOfBirth: Date, //need to change this
    species: string,
    breed: string,
    description: string,
    gender: string, //male,female, dropdown??
    hasError: boolean,
    error: string,
    added: boolean
}
class AddPet extends Component<IProps, IState> {

    constructor(props: IProps) {
        super(props);
        this.state = {
            name: '',
            dateOfBirth: new Date(),
            species: '',
            breed: '',
            description: '',
            gender: 'MALE',
            hasError: false,
            error: '',
            added: false
        };
        this.handleName = this.handleName.bind(this);
        this.handleDateOfBirth = this.handleDateOfBirth.bind(this);
        this.handleSpecies = this.handleSpecies.bind(this);
        this.handleBreed = this.handleBreed.bind(this);
        this.handleDescription = this.handleDescription.bind(this);
        this.handleGender = this.handleGender.bind(this);

        this.submitForm = this.submitForm.bind(this);
    }

    // handleEmail(event) {
    //     this.setState({email: event.target.value});
    // }
    handleName(event) {
        this.setState({name: event.target.value});
    }
    handleDateOfBirth(event) {
        this.setState({dateOfBirth: event.value});
    }
    handleSpecies(event) {
        this.setState({species: event.target.value});
    }
    handleBreed(event) {
        this.setState({breed: event.target.value});
    }
    handleDescription(event) {
        this.setState({description: event.target.value});
    }
    handleGender(event) {
        this.setState({gender: event.target.value});
    }


    render(){
        return <Container component="main" maxWidth="xs">
            <CssBaseline/>
            <div style={{
                marginTop: "10%",
                display: 'flex',
                flexDirection: 'column',
                alignItems: 'center'
            }}>
                <Avatar style={{margin: "2%",
                    backgroundColor: "#2BE0A2",}}>
                    <PetsIcon/>
                </Avatar>
                <Typography component="h1" variant="h5" style={{color: "black"}}>
                    Add new pet
                </Typography>
                {this.state.added &&
                <Typography component="h1" variant="h5" style={{color: "black"}}>
                    Pet added!
                </Typography>}
                <form style={{
                    width: '100%',
                    marginTop: "2%"
                }} noValidate onSubmit={this.submitForm}>
                    <Grid container spacing={2}>
                        <Grid item xs={12}>
                            <label>Name:</label>
                            <TextField
                                variant="outlined"
                                id="name"
                                label="name"
                                name="name"
                                autoComplete="name"
                                onChange={this.handleName}
                                value={this.state.name}
                            />
                        </Grid>
                        <Grid item xs={12}>
                            <label>Date of Birth:</label>
                            <TextField
                                id="dateOfBirth"
                                type="date"
                                defaultValue="2020-01-01"
                                InputLabelProps={{
                                    shrink: true,
                                }}
                            />
                        </Grid>
                        <Grid item xs={12}>
                            <label>Species:</label>
                            <TextField
                                variant="outlined"
                                name="species"
                                label="species"
                                type="species"
                                id="species"
                                autoComplete="species"
                                onChange={this.handleSpecies}
                                value={this.state.species}
                            />
                        </Grid>
                        <Grid item xs={12}>
                            <label>Breed:</label>
                            <TextField
                                variant="outlined"
                                name="breed"
                                label="breed"
                                type="breed"
                                id="breed"
                                autoComplete="breed"
                                onChange={this.handleBreed}
                                value={this.state.breed}
                            />
                        </Grid>
                        <Grid item xs={12}>
                            <label>Description:</label>
                            <TextField
                                variant="outlined"
                                name="description"
                                label="description"
                                type="description"
                                id="description"
                                autoComplete="description"
                                onChange={this.handleDescription}
                                value={this.state.description}
                            />
                        </Grid>
                        <Grid item xs={12}>
                            <div className="radio">
                                <label>Gender:</label>
                                <label>
                                    <input type="radio" value="MALE" checked={this.state.gender === 'MALE'}
                                           onChange={this.handleGender}/>
                                    Male
                                </label>
                                <label>
                                    <input type="radio" value="FEMALE" checked={this.state.gender === 'FEMALE'}
                                           onChange={this.handleGender}/>
                                    Female
                                </label>
                            </div>

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
                        Submit
                    </Button>
                    {this.state.hasError && <p style={{color: "red", fontSize: "0.7em", fontWeight: "bold"}}>
                        {this.state.error}
                    </p>}
                </form>
            </div>
            <Box mt={5}>
            </Box>
        </Container>;
    };

    submitForm(event) {
        fetch("http://petshelter-backend.herokuapp.com/api/pet/add",{
            method: 'post',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                name: this.state.name,
                dateOfBirth: this.state.dateOfBirth,
                species: this.state.species,
                breed: this.state.breed,
                description: this.state.description,
                gender: this.state.gender
            })
        }).then((response) =>{
            if(response.status===201) {
                console.log(response);
                this.setState({hasError: false});
                this.setState({error: ''});
                this.setState({added: true});
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


}

export default AddPet;
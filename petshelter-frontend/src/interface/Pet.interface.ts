import PetSexInterface from "./PetSex.interface";
import UserInterface from "./User.interface";

export default interface PetInterface{
    name:string;
    species:string;
    dateOfBirth:string; //check if string will work
    picture:Array<string> //a bet can have multiple pictures
    breed:string;
    description:string;
    PetId:number;
    gender:PetSexInterface
    AdId:number;
    user:UserInterface;
}
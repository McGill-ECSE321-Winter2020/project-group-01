import UserInterface from "./User.interface";

export default interface CommentInterface {
    author: UserInterface
    text: String
    timePosted: Date
}
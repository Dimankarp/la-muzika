import { reactive } from "vue"

export const userStore = reactive({
    isLoggedIn: false,
    userId: 0,
    userName: "",
    isAdmin: false,
    setLogOut(){
        this.isLoggedIn = false;
    },
    setName(username){
        this.userName = username;
    },
    login(dto){
        this.isLoggedIn = true;
        this.userId = dto.id
        this.userName = dto.username
        this.isAdmin = dto.isAdmin
        window.localStorage.setItem("username", dto.username)
    }
})
import { reactive } from "vue"
export const userStore = reactive({
    isLoggedIn: localStorage.getItem('isLoggedIn')==='true', //Warining! Important for refresh
    userId: 0,
    username: "",
    isAdmin: false,
    logout(){
        this.isLoggedIn = false;
        localStorage.setItem('isLoggedIn', false)
        this.userId =  0;
        this.username = "";
        this.isAdmin = false;
    },
    login(dto){
        this.isLoggedIn = true;
        localStorage.setItem('isLoggedIn', true)
        this.userId = dto.id
        this.username = dto.username
        this.isAdmin = dto.admin
         }
})
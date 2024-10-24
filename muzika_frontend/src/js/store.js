import { reactive } from "vue"
import { fetchUser } from "@/js/auth";
export const userStore = reactive({
    isLoggedIn: true, //Warining! Important for refresh
    userId: 0,
    username: "",
    isAdmin: false,
    logout(){
        this.isLoggedIn = false;
        this.userId =  0;
        this.username = "";
        this.isAdmin = false;
    },
    login(dto){
        this.isLoggedIn = true;
        this.userId = dto.id
        this.username = dto.username
        this.isAdmin = dto.admin
         }
})
import { userStore } from "./store";

export async function tryLogin(){
    const username = window.localStorage.getItem("username")
    if(username == null) return false;
    const response = await fetch(`/api/users/${username}`, {
        method:"GET",
        credentials: "same-origin"
    })
    if(response.ok){
        return true;
    }
    return false;
}

export async function updateUser(username){
    const response = await fetch(`/api/users/${username}`, {
        method:"GET",
        credentials: "same-origin"
    })
    if(response.ok){
        userStore.login(await response.json());
    } else {
        throw "Faild to update User"
    }

}
export async function logout() {
    return await fetch("/api/auth/logout", {
        method:"POST",
        credentials: "same-origin"
    }).finally(()=>{
        userStore.setLogOut()
        window.localStorage.clear()
    })
}
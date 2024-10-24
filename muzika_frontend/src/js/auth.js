import { useRouter } from "vue-router";
import { userStore } from "./store";


export async function fetchUser() {
    const response = await fetch("/api/auth/account", {
        method: "GET",
        credentials: "same-origin"
    })
    if (response.ok) {
        userStore.login(await response.json());
        return true
    } else {
        return false
    }

}
export async function logout() {
    return await fetch("/api/auth/logout", {
        method: "POST",
        credentials: "same-origin"
    }).finally(() => {
        userStore.logout()
    })
}

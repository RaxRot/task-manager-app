import axios from 'axios';

export default class ApiService {
    static API_URL = 'http://localhost:8080/api';

    static saveToken(token) {
        localStorage.setItem('token', token);
    }

    static getToken() {
       return  localStorage.getItem('token');
    }

    static isAuthenticated() {
        return !!localStorage.getItem('token');
    }

    static logout() {
        localStorage.removeItem('token');
    }

    static getHeader(){
        const token=this.getToken();
        return{
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
        }
    }

    //REGISTER USER
    static async registerUser(body){
        const response = await axios.post(`${this.API_URL}/auth/register`, body);
        return response.data;
    }

    //LOGIN USER
    static async loginUser(body){
        const response = await axios.post(`${this.API_URL}/auth/login`, body);
        return response.data;
    }

    //TASK API

    static async createTask(body){
        const response=await axios.post(`${this.API_URL}/tasks`, body, {headers:this.getHeader()});
        return response.data;
    }

    static async getAllTasks(){
        const response=await axios.get(`${this.API_URL}/tasks`, {headers:this.getHeader()});
        return response.data;
    }

    static async getTaskById(id){
        const response=await axios.get(`${this.API_URL}/tasks/${id}`, {headers:this.getHeader()});
        return response.data;
    }

    static async updateTask(id, body){
        const response=await axios.put(`${this.API_URL}/tasks/${id}`, body, {headers:this.getHeader()});
        return response.data;
    }

    static async deleteTask(id){
        const response=await axios.delete(`${this.API_URL}/tasks/${id}`, {headers:this.getHeader()});
        return response.data;
    }

    static async getTasksByCompletedStatus(completed) {
        const response = await axios.get(`${this.API_URL}/tasks/status`, {
            headers: this.getHeader(),
            params: { completed }
        });
        return response.data;
    }

    static async getTasksByPriority(priority) {
        const response = await axios.get(`${this.API_URL}/tasks/priority`, {
            headers: this.getHeader(),
            params: { priority }
        });
        return response.data;
    }
}
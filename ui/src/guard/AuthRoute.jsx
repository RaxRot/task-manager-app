import {useLocation} from "react-router-dom";
import ApiService from "../api/ApiService.js";

export default function AuthRoute({ children }) {
    const location = useLocation();

    return ApiService.isAuthenticated() ? (
        children
    ) : (
        <Navigate to="/login" state={{ from: location }} replace />
    );
}
import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {PageDataHandler} from "../../utils/PageDataHandler";
import {environment} from "../../../environments/environment";
import {Config} from "./config.component";

@Injectable()
export class ConfigService {
    private configUrl: string = environment.configUrl;

    constructor(private http: HttpClient) {
    }

    getConfig(): Observable<Config> {
        return this.http.get(this.configUrl).filter(PageDataHandler.successResponseFilter)
            .map(data => data.configs)
    }

    setConfigValue(json) {
        return this.http.put(this.configUrl, json)
    }
}

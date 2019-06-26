import {NativeModules} from "react-native";

const RNYandexMapKitModule = NativeModules.RNYandexMapKit;

export namespace MapKit {
    export function setApiKey(apiKey: string): void {
        RNYandexMapKitModule.setApiKey(apiKey);
    }
}
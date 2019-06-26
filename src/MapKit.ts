import {NativeModules} from "react-native";

const RNYandexMapKitModule = NativeModules.RNYandexMapKit;

export class MapKit {
    public static setApiKey(apiKey: string): void {
        RNYandexMapKitModule.setApiKey(apiKey);
    }
}
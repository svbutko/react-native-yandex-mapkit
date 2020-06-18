import {Component} from "react";

export {
    MapViewProps,
    BoundingBox,
    Polygon,
    Region,
    LatLng,
    Point,
    MarkerProps,
    SuggestionsResult,
    Suggestion,
    MarkerData,
    DeviceLocation,
    IconType
} from "./src/MapViewTypes";

declare module "react-native-yandex-mapkit" {
    export default class MapView<T, B> extends Component<Partial<MapViewProps<T, B>>> {
        public static setApiKey(apiKey: string): void;

        public static setLocale(locale: string): void;

        public navigateToRegion(region: Region, isAnimated?: boolean): void;

        public navigateToBoundingBox(northEastPoint: LatLng, southWestPoint: LatLng): void;

        public zoomIn(): void;

        public zoomOut(): void;

        public navigateToUserLocation(): void;

        public getUserLocation(): void;

        public fetchSuggestions(query: string): void;

        public stopMapKit(): void;
    }
}

/*tslint:disable:interface-name*/
import {NativeSyntheticEvent, StyleProp, UIManager, ViewStyle} from "react-native";
import {PureComponent} from "react";

export interface MapViewProps<T, B> {
    markers?: MarkerProps<T>[];
    polygons?: Polygon<B>[];
    initialRegion?: Region;
    onMarkerPress?: (data: MarkerData<T>) => void;
    onPolygonPress?: (data: MarkerData<B>) => void;
    onMapPress?: (data: unknown) => void;
    onLocationSearch?: (data: unknown) => void;
    onSuggestionsFetch?: (data: SuggestionsResult) => void;
    onDeviceLocationSearch?: (data: DeviceLocation) => void;
    searchLocation?: boolean;
    searchRoute?: MarkerProps<T>[];
    searchMarker?: MarkerProps<T>;
    style?: StyleProp<ViewStyle>;
    boundingBox?: BoundingBox;
    disableMarkers?: boolean;
}

export interface NativeMapViewProps<T, B> extends Omit<MapViewProps<T, B>, "onMarkerPress" | "onPolygonPress" | "onDeviceLocationSearch" | "onSuggestionsFetch"> {
    onMarkerPress?: (event: NativeSyntheticEvent<MarkerData<T>>) => void;
    onPolygonPress?: (event: NativeSyntheticEvent<MarkerData<B>>) => void;
    onDeviceLocationSearch?: (event: NativeSyntheticEvent<DeviceLocation>) => void;
    onSuggestionsFetch?: (event: NativeSyntheticEvent<SuggestionsResult>) => void;
}

export interface BoundingBox {
    northEastPoint: LatLng;
    southWestPoint: LatLng;
}

export interface Polygon<B> {
    userData?: B;
    points: LatLng[];
    backgroundColor?: string;
    borderColor?: string;
}

export interface Region {
    latitude: number;
    longitude: number;
    latitudeDelta: number;
    longitudeDelta: number;
}

export interface LatLng {
    latitude: number;
    longitude: number;
}

export interface Point {
    x: number;
    y: number;
}

export type IconType = "pin" | "selectedPin" | "user" | "disabled";

export interface MarkerProps<T> {
    coordinate: LatLng;
    icon?: IconType;
    opacity?: number;
    centerOffset?: Point;
    calloutOffset?: Point;
    draggable?: boolean;
    userData?: T;
}

export interface SuggestionsResult {
    suggestions: Suggestion[];
}

export interface Suggestion {
    value: string;
}

export interface MarkerData<B> extends LatLng {
    data?: B;
}

export interface DeviceLocation extends LatLng {
    location: string;
}

type MapViewCommands =
    "stopMapKit" |
    "navigateToUserLocation" |
    "getUserLocation" |
    "zoomIn" | "zoomOut" |
    "navigateToRegion" |
    "navigateToBoundingBox" |
    "fetchSuggestions";

interface IRNYandexMapKitUIManager<Commands extends string> extends UIManager {
    RNYandexMapKit: {
        Commands: { [key in Commands]: number }
    };
}

export type RNYandexMapKitUIManager = IRNYandexMapKitUIManager<MapViewCommands>;

export class NativeMapKitComponent extends PureComponent<NativeMapViewProps<unknown, unknown>> {
}

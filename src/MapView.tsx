import React, {PureComponent} from "react";
import {
    findNodeHandle,
    NativeModules,
    NativeSyntheticEvent,
    processColor,
    requireNativeComponent,
    UIManager as NotTypedUIManager
} from "react-native";
import {
    DeviceLocation,
    LatLng,
    MapViewProps,
    MarkerData,
    MarkerProps,
    NativeMapKitComponent,
    Polygon,
    Region,
    RNYandexMapKitUIManager,
    SuggestionsResult
} from "./MapViewTypes";

const RNYandexMapKit: typeof NativeMapKitComponent = requireNativeComponent("RNYandexMapKit");
const RNYandexMapKitModule = NativeModules.RNYandexMapKit;
const UIManager = NotTypedUIManager as RNYandexMapKitUIManager;

interface IProps extends MapViewProps<unknown, unknown> {}

export class MapView extends PureComponent<IProps> {
    private lastMarkers: MarkerProps<unknown>[] | undefined;
    private lastPolygons: Polygon<unknown>[] | undefined;
    private markers: MarkerProps<unknown>[] | undefined;
    private polygons: Polygon<unknown>[] | undefined;

    constructor(props: IProps) {
        super(props);

        this.cacheProps = this.cacheProps.bind(this);
        this._onLocationSearch = this._onLocationSearch.bind(this);
        this._onMarkerPress = this._onMarkerPress.bind(this);
        this._onMapPress = this._onMapPress.bind(this);
        this._onPolygonPress = this._onPolygonPress.bind(this);
        this._onSuggestionsFetch = this._onSuggestionsFetch.bind(this);
        this._onDeviceLocationSearch = this._onDeviceLocationSearch.bind(this);
    }

    public static setApiKey(apiKey: string): void {
        RNYandexMapKitModule.setApiKey(apiKey);
    }

    public static setLocale(locale: string): void {
        RNYandexMapKitModule.setLocale(locale);
    }

    render(): JSX.Element {
        this.cacheProps();

        return (
            <RNYandexMapKit
                {...this.props}
                markers={this.markers}
                polygons={this.polygons}
                onLocationSearch={this._onLocationSearch}
                onMapPress={this._onMapPress}
                onMarkerPress={this._onMarkerPress}
                onPolygonPress={this._onPolygonPress}
                onDeviceLocationSearch={this._onDeviceLocationSearch}
                onSuggestionsFetch={this._onSuggestionsFetch}
            />
        );
    }

    private cacheProps(): void {
        const {polygons, markers, disableMarkers} = this.props;

        if (this.lastPolygons != polygons) {
            this.polygons = this.props.polygons && this.props.polygons.map((item): Polygon<unknown> => ({
                ...item,
                backgroundColor: processColor(item.backgroundColor).toString(),
                borderColor: processColor(item.borderColor).toString(),
            }));
        }

        this.lastPolygons = polygons;

        if (disableMarkers) {
            if (this.lastMarkers != markers) {
                this.markers = this.props.markers && this.props.markers.map((item): MarkerProps<unknown> => {
                    const {userData, ...markerProps} = item;

                    return markerProps;
                });
            } else {
                this.lastMarkers = markers;
            }
        } else {
            this.markers = markers;
        }
    }

    private _onSuggestionsFetch(event: NativeSyntheticEvent<SuggestionsResult>): void {
        this.props.onSuggestionsFetch && this.props.onSuggestionsFetch(event.nativeEvent);
    }

    private _onLocationSearch(event: NativeSyntheticEvent<unknown>): void {
        this.props.onLocationSearch && this.props.onLocationSearch(event.nativeEvent);
    }

    private _onMarkerPress(event: NativeSyntheticEvent<MarkerData<unknown>>): void {
        this.props.onMarkerPress && this.props.onMarkerPress(event.nativeEvent);
    }

    private _onMapPress(event: NativeSyntheticEvent<unknown>): void {
        this.props.onMapPress && this.props.onMapPress(event.nativeEvent);
    }

    private _onPolygonPress(event: NativeSyntheticEvent<MarkerData<unknown>>): void {
        this.props.onPolygonPress && this.props.onPolygonPress(event.nativeEvent);
    }

    private _onDeviceLocationSearch(event: NativeSyntheticEvent<DeviceLocation>): void {
        this.props.onDeviceLocationSearch && this.props.onDeviceLocationSearch(event.nativeEvent);
    }

    public stopMapKit(): void {
        UIManager.dispatchViewManagerCommand(
            findNodeHandle(this),
            UIManager.RNYandexMapKit.Commands.stopMapKit,
            [],
        );
    }

    public navigateToUserLocation(): void {
        UIManager.dispatchViewManagerCommand(
            findNodeHandle(this),
            UIManager.RNYandexMapKit.Commands.navigateToUserLocation,
            [],
        );
    }

    public getUserLocation(): void {
        UIManager.dispatchViewManagerCommand(
            findNodeHandle(this),
            UIManager.RNYandexMapKit.Commands.getUserLocation,
            [],
        );
    }

    public zoomIn(): void {
        UIManager.dispatchViewManagerCommand(
            findNodeHandle(this),
            UIManager.RNYandexMapKit.Commands.zoomIn,
            [],
        );
    }

    public zoomOut(): void {
        UIManager.dispatchViewManagerCommand(
            findNodeHandle(this),
            UIManager.RNYandexMapKit.Commands.zoomOut,
            [],
        );
    }

    public navigateToRegion(region: Region, isAnimatedParam?: boolean): void {
        const isAnimated = isAnimatedParam ? isAnimatedParam : false;
        UIManager.dispatchViewManagerCommand(
            findNodeHandle(this),
            UIManager.RNYandexMapKit.Commands.navigateToRegion,
            [region, isAnimated],
        );
    }

    public navigateToBoundingBox(northEastPoint: LatLng, southWestPoint: LatLng): void {
        UIManager.dispatchViewManagerCommand(
            findNodeHandle(this),
            UIManager.RNYandexMapKit.Commands.navigateToBoundingBox,
            [northEastPoint, southWestPoint],
        );
    }

    public fetchSuggestions(query: string): void {
        UIManager.dispatchViewManagerCommand(
            findNodeHandle(this),
            UIManager.RNYandexMapKit.Commands.fetchSuggestions,
            [query],
        );
    }
}

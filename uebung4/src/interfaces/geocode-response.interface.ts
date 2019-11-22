export interface IGeocodeResponse {
  Response: {
    MetaInfo: {
      Timestamp: string;
    },
    View: Array<{
      _type: string,
      ViewId: number,
      Result: Array<{
        Relevance: number,
        MatchLevel: string,
        MatchQuality: {
          City: number,
          Street: number,
          HouseNumber: number,
        },
        MatchType: string,
        Location: {
          LocationId: string,
          LocationType: string,
          DisplayPosition: {
            Latitude: number,
            Longitude: number,
          },
          NavigationPosition: Array<{
            Latitude: number,
            Longitude: number,
          }>,
          // More is available but not needed
        },
      }>,
    }>,
  };
}

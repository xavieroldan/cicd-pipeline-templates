# Build stage
FROM mcr.microsoft.com/dotnet/sdk:8.0 AS build
WORKDIR /src
COPY src/SampleApi/SampleApi.csproj src/SampleApi/
RUN dotnet restore src/SampleApi/SampleApi.csproj
COPY src/ src/
RUN dotnet publish src/SampleApi/SampleApi.csproj -c Release -o /app --no-restore

# Runtime stage
FROM mcr.microsoft.com/dotnet/aspnet:8.0
WORKDIR /app
COPY --from=build /app .
ENV ASPNETCORE_URLS=http://+:8080
EXPOSE 8080
USER app
ENTRYPOINT ["dotnet", "SampleApi.dll"]

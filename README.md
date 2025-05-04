# Build
So far empty...

# Run
So far empty...

# Profiles
- `|` - separate exchangeable profiles
- `,` - separate required profiles

`cloud | local, dev | prod` expression equal the next expression: `(cloud or local) and (dev or prod)`

| Microservice name   | Profiles                    |
|---------------------|-----------------------------|
| akhs-configurations | git \| native               |
| akhs-discord        | cloud \| local, dev \| prod |
| akhs-telegram       | cloud \| local, dev \| prod |
| akhs-twitch         | cloud \| local, dev \| prod |
| akhs-youtube        | cloud \| local, dev \| prod |

If you change a `native` profile to `akhs-configurations`,
you should change a `local` profile for `akhs-discord`, `akhs-telegram`, `akhs-twitch`, `akhs-youtube`

If you change a `git` profile to `akhs-configurations`,
you should change a `cloud` profile for `akhs-discord`, `akhs-telegram`, `akhs-twitch`, `akhs-youtube`
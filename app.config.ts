import { ConfigContext, ExpoConfig } from '@expo/config';
import 'dotenv/config';
import moment from 'moment';

export default ({ config }: ConfigContext): ExpoConfig => {
  return {
    ...config,
    name: config.name || '',
    slug: config.slug || '',
    extra: {
      ...config.extra,
      version: moment.utc().format('YYYYMMDD-HHmm'),
    },
  };
};

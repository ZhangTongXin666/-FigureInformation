package dataBean;

import java.util.List;

/**
 * Created by 13717 on 2017/8/16.
 */

public class WheatherBean {

    /**
     * result : {"realtime":{"wind":{"windspeed":null,"direct":"南风","power":"2级","offset":null},"time":"12:00:00","weather":{"humidity":"76","img":"1","info":"多云","temperature":"27"},"dataUptime":"1502857021","date":"2017-08-16","city_code":"101010100","city_name":"北京","week":"3","moon":"六月廿五"},"life":{"date":"2017-8-16","info":{"kongtiao":["部分时间开启","天气热，到中午的时候您将会感到有点热，因此建议在午后较热时开启制冷空调。"],"yundong":["较不宜","有降水，推荐您在室内进行低强度运动；若坚持户外运动，须注意选择避雨防滑并携带雨具。"],"ziwaixian":["弱","紫外线强度较弱，建议出门前涂擦SPF在12-15之间、PA+的防晒护肤品。"],"ganmao":["少发","各项气象条件适宜，发生感冒机率较低。但请避免长期处于空调房间中，以防感冒。"],"xiche":["不宜","不宜洗车，未来24小时内有雨，如果在此期间洗车，雨水和路上的泥水可能会再次弄脏您的爱车。"],"wuran":null,"chuanyi":["热","天气热，建议着短裙、短裤、短薄外套、T恤等夏季服装。"]}},"weather":[{"date":"2017-08-16","week":"三","nongli":"闰六月廿五","info":{"dawn":null,"day":["4","雷阵雨","30","北风","微风","05:28","出门记得带伞，行走驾驶做好防滑准备"],"night":["2","阴","22","北风","微风","19:09","出门记得带伞，行走驾驶做好防滑准备"]}},{"date":"2017-08-17","week":"四","nongli":"闰六月廿六","info":{"dawn":["2","阴","22","北风","微风","19:09"],"day":["2","阴","29","东北风","微风","05:29"],"night":["2","阴","22","东北风","微风","19:08"]}},{"date":"2017-08-18","week":"五","nongli":"闰六月廿七","info":{"dawn":["2","阴","22","东北风","微风","19:08"],"day":["1","多云","29","东风","微风","05:30"],"night":["1","多云","23","东北风","微风","19:07"]}},{"date":"2017-08-19","week":"六","nongli":"闰六月廿八","info":{"dawn":["1","多云","23","东北风","微风","19:07"],"day":["1","多云","28","北风","微风","05:31"],"night":["1","多云","22","东南风","微风","19:05"]}},{"date":"2017-08-20","week":"日","nongli":"闰六月廿九","info":{"dawn":["1","多云","22","东南风","微风","19:05"],"day":["2","阴","29","南风","微风","05:32"],"night":["4","雷阵雨","23","南风","微风","19:04"]}},{"date":"2017-08-21","week":"一","nongli":"闰六月三十","info":{"dawn":null,"day":["4","雷阵雨","29","东南风","微风","07:30"],"night":["3","阵雨","22","东南风","微风","19:30"]}},{"date":"2017-08-22","week":"二","nongli":"七月初一","info":{"dawn":null,"day":["3","阵雨","28","东南风","微风","07:30"],"night":["3","阵雨","22","东南风","微风","19:30"]}}],"pm25":{"key":"Beijing","show_desc":"0","pm25":{"curPm":"125","pm25":"90","pm10":"107","level":"3","quality":"轻度污染","des":"敏感人群应避免高强度户外锻炼，外出时做好防护措施"},"dateTime":"2017年08月16日12时","cityName":"北京"},"isForeign":0}
     * error_code : 0
     * reason : Succes
     */

    private ResultBean result;
    private int error_code;
    private String reason;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public static class ResultBean {
        /**
         * realtime : {"wind":{"windspeed":null,"direct":"南风","power":"2级","offset":null},"time":"12:00:00","weather":{"humidity":"76","img":"1","info":"多云","temperature":"27"},"dataUptime":"1502857021","date":"2017-08-16","city_code":"101010100","city_name":"北京","week":"3","moon":"六月廿五"}
         * life : {"date":"2017-8-16","info":{"kongtiao":["部分时间开启","天气热，到中午的时候您将会感到有点热，因此建议在午后较热时开启制冷空调。"],"yundong":["较不宜","有降水，推荐您在室内进行低强度运动；若坚持户外运动，须注意选择避雨防滑并携带雨具。"],"ziwaixian":["弱","紫外线强度较弱，建议出门前涂擦SPF在12-15之间、PA+的防晒护肤品。"],"ganmao":["少发","各项气象条件适宜，发生感冒机率较低。但请避免长期处于空调房间中，以防感冒。"],"xiche":["不宜","不宜洗车，未来24小时内有雨，如果在此期间洗车，雨水和路上的泥水可能会再次弄脏您的爱车。"],"wuran":null,"chuanyi":["热","天气热，建议着短裙、短裤、短薄外套、T恤等夏季服装。"]}}
         * weather : [{"date":"2017-08-16","week":"三","nongli":"闰六月廿五","info":{"dawn":null,"day":["4","雷阵雨","30","北风","微风","05:28","出门记得带伞，行走驾驶做好防滑准备"],"night":["2","阴","22","北风","微风","19:09","出门记得带伞，行走驾驶做好防滑准备"]}},{"date":"2017-08-17","week":"四","nongli":"闰六月廿六","info":{"dawn":["2","阴","22","北风","微风","19:09"],"day":["2","阴","29","东北风","微风","05:29"],"night":["2","阴","22","东北风","微风","19:08"]}},{"date":"2017-08-18","week":"五","nongli":"闰六月廿七","info":{"dawn":["2","阴","22","东北风","微风","19:08"],"day":["1","多云","29","东风","微风","05:30"],"night":["1","多云","23","东北风","微风","19:07"]}},{"date":"2017-08-19","week":"六","nongli":"闰六月廿八","info":{"dawn":["1","多云","23","东北风","微风","19:07"],"day":["1","多云","28","北风","微风","05:31"],"night":["1","多云","22","东南风","微风","19:05"]}},{"date":"2017-08-20","week":"日","nongli":"闰六月廿九","info":{"dawn":["1","多云","22","东南风","微风","19:05"],"day":["2","阴","29","南风","微风","05:32"],"night":["4","雷阵雨","23","南风","微风","19:04"]}},{"date":"2017-08-21","week":"一","nongli":"闰六月三十","info":{"dawn":null,"day":["4","雷阵雨","29","东南风","微风","07:30"],"night":["3","阵雨","22","东南风","微风","19:30"]}},{"date":"2017-08-22","week":"二","nongli":"七月初一","info":{"dawn":null,"day":["3","阵雨","28","东南风","微风","07:30"],"night":["3","阵雨","22","东南风","微风","19:30"]}}]
         * pm25 : {"key":"Beijing","show_desc":"0","pm25":{"curPm":"125","pm25":"90","pm10":"107","level":"3","quality":"轻度污染","des":"敏感人群应避免高强度户外锻炼，外出时做好防护措施"},"dateTime":"2017年08月16日12时","cityName":"北京"}
         * isForeign : 0
         */

        private RealtimeBean realtime;
        private LifeBean life;
        private Pm25BeanX pm25;
        private int isForeign;
        private List<WeatherBeanX> weather;

        public RealtimeBean getRealtime() {
            return realtime;
        }

        public void setRealtime(RealtimeBean realtime) {
            this.realtime = realtime;
        }

        public LifeBean getLife() {
            return life;
        }

        public void setLife(LifeBean life) {
            this.life = life;
        }

        public Pm25BeanX getPm25() {
            return pm25;
        }

        public void setPm25(Pm25BeanX pm25) {
            this.pm25 = pm25;
        }

        public int getIsForeign() {
            return isForeign;
        }

        public void setIsForeign(int isForeign) {
            this.isForeign = isForeign;
        }

        public List<WeatherBeanX> getWeather() {
            return weather;
        }

        public void setWeather(List<WeatherBeanX> weather) {
            this.weather = weather;
        }

        public static class RealtimeBean {
            /**
             * wind : {"windspeed":null,"direct":"南风","power":"2级","offset":null}
             * time : 12:00:00
             * weather : {"humidity":"76","img":"1","info":"多云","temperature":"27"}
             * dataUptime : 1502857021
             * date : 2017-08-16
             * city_code : 101010100
             * city_name : 北京
             * week : 3
             * moon : 六月廿五
             */

            private WindBean wind;
            private String time;
            private WeatherBean weather;
            private String dataUptime;
            private String date;
            private String city_code;
            private String city_name;
            private String week;
            private String moon;

            public WindBean getWind() {
                return wind;
            }

            public void setWind(WindBean wind) {
                this.wind = wind;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public WeatherBean getWeather() {
                return weather;
            }

            public void setWeather(WeatherBean weather) {
                this.weather = weather;
            }

            public String getDataUptime() {
                return dataUptime;
            }

            public void setDataUptime(String dataUptime) {
                this.dataUptime = dataUptime;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getCity_code() {
                return city_code;
            }

            public void setCity_code(String city_code) {
                this.city_code = city_code;
            }

            public String getCity_name() {
                return city_name;
            }

            public void setCity_name(String city_name) {
                this.city_name = city_name;
            }

            public String getWeek() {
                return week;
            }

            public void setWeek(String week) {
                this.week = week;
            }

            public String getMoon() {
                return moon;
            }

            public void setMoon(String moon) {
                this.moon = moon;
            }

            public static class WindBean {
                /**
                 * windspeed : null
                 * direct : 南风
                 * power : 2级
                 * offset : null
                 */

                private Object windspeed;
                private String direct;
                private String power;
                private Object offset;

                public Object getWindspeed() {
                    return windspeed;
                }

                public void setWindspeed(Object windspeed) {
                    this.windspeed = windspeed;
                }

                public String getDirect() {
                    return direct;
                }

                public void setDirect(String direct) {
                    this.direct = direct;
                }

                public String getPower() {
                    return power;
                }

                public void setPower(String power) {
                    this.power = power;
                }

                public Object getOffset() {
                    return offset;
                }

                public void setOffset(Object offset) {
                    this.offset = offset;
                }
            }

            public static class WeatherBean {
                /**
                 * humidity : 76
                 * img : 1
                 * info : 多云
                 * temperature : 27
                 */

                private String humidity;
                private String img;
                private String info;
                private String temperature;

                public String getHumidity() {
                    return humidity;
                }

                public void setHumidity(String humidity) {
                    this.humidity = humidity;
                }

                public String getImg() {
                    return img;
                }

                public void setImg(String img) {
                    this.img = img;
                }

                public String getInfo() {
                    return info;
                }

                public void setInfo(String info) {
                    this.info = info;
                }

                public String getTemperature() {
                    return temperature;
                }

                public void setTemperature(String temperature) {
                    this.temperature = temperature;
                }
            }
        }

        public static class LifeBean {
            /**
             * date : 2017-8-16
             * info : {"kongtiao":["部分时间开启","天气热，到中午的时候您将会感到有点热，因此建议在午后较热时开启制冷空调。"],"yundong":["较不宜","有降水，推荐您在室内进行低强度运动；若坚持户外运动，须注意选择避雨防滑并携带雨具。"],"ziwaixian":["弱","紫外线强度较弱，建议出门前涂擦SPF在12-15之间、PA+的防晒护肤品。"],"ganmao":["少发","各项气象条件适宜，发生感冒机率较低。但请避免长期处于空调房间中，以防感冒。"],"xiche":["不宜","不宜洗车，未来24小时内有雨，如果在此期间洗车，雨水和路上的泥水可能会再次弄脏您的爱车。"],"wuran":null,"chuanyi":["热","天气热，建议着短裙、短裤、短薄外套、T恤等夏季服装。"]}
             */

            private String date;
            private InfoBean info;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public InfoBean getInfo() {
                return info;
            }

            public void setInfo(InfoBean info) {
                this.info = info;
            }

            public static class InfoBean {
                /**
                 * kongtiao : ["部分时间开启","天气热，到中午的时候您将会感到有点热，因此建议在午后较热时开启制冷空调。"]
                 * yundong : ["较不宜","有降水，推荐您在室内进行低强度运动；若坚持户外运动，须注意选择避雨防滑并携带雨具。"]
                 * ziwaixian : ["弱","紫外线强度较弱，建议出门前涂擦SPF在12-15之间、PA+的防晒护肤品。"]
                 * ganmao : ["少发","各项气象条件适宜，发生感冒机率较低。但请避免长期处于空调房间中，以防感冒。"]
                 * xiche : ["不宜","不宜洗车，未来24小时内有雨，如果在此期间洗车，雨水和路上的泥水可能会再次弄脏您的爱车。"]
                 * wuran : null
                 * chuanyi : ["热","天气热，建议着短裙、短裤、短薄外套、T恤等夏季服装。"]
                 */

                private Object wuran;
                private List<String> kongtiao;
                private List<String> yundong;
                private List<String> ziwaixian;
                private List<String> ganmao;
                private List<String> xiche;
                private List<String> chuanyi;

                public Object getWuran() {
                    return wuran;
                }

                public void setWuran(Object wuran) {
                    this.wuran = wuran;
                }

                public List<String> getKongtiao() {
                    return kongtiao;
                }

                public void setKongtiao(List<String> kongtiao) {
                    this.kongtiao = kongtiao;
                }

                public List<String> getYundong() {
                    return yundong;
                }

                public void setYundong(List<String> yundong) {
                    this.yundong = yundong;
                }

                public List<String> getZiwaixian() {
                    return ziwaixian;
                }

                public void setZiwaixian(List<String> ziwaixian) {
                    this.ziwaixian = ziwaixian;
                }

                public List<String> getGanmao() {
                    return ganmao;
                }

                public void setGanmao(List<String> ganmao) {
                    this.ganmao = ganmao;
                }

                public List<String> getXiche() {
                    return xiche;
                }

                public void setXiche(List<String> xiche) {
                    this.xiche = xiche;
                }

                public List<String> getChuanyi() {
                    return chuanyi;
                }

                public void setChuanyi(List<String> chuanyi) {
                    this.chuanyi = chuanyi;
                }
            }
        }

        public static class Pm25BeanX {
            /**
             * key : Beijing
             * show_desc : 0
             * pm25 : {"curPm":"125","pm25":"90","pm10":"107","level":"3","quality":"轻度污染","des":"敏感人群应避免高强度户外锻炼，外出时做好防护措施"}
             * dateTime : 2017年08月16日12时
             * cityName : 北京
             */

            private String key;
            private String show_desc;
            private Pm25Bean pm25;
            private String dateTime;
            private String cityName;

            public String getKey() {
                return key;
            }

            public void setKey(String key) {
                this.key = key;
            }

            public String getShow_desc() {
                return show_desc;
            }

            public void setShow_desc(String show_desc) {
                this.show_desc = show_desc;
            }

            public Pm25Bean getPm25() {
                return pm25;
            }

            public void setPm25(Pm25Bean pm25) {
                this.pm25 = pm25;
            }

            public String getDateTime() {
                return dateTime;
            }

            public void setDateTime(String dateTime) {
                this.dateTime = dateTime;
            }

            public String getCityName() {
                return cityName;
            }

            public void setCityName(String cityName) {
                this.cityName = cityName;
            }

            public static class Pm25Bean {
                /**
                 * curPm : 125
                 * pm25 : 90
                 * pm10 : 107
                 * level : 3
                 * quality : 轻度污染
                 * des : 敏感人群应避免高强度户外锻炼，外出时做好防护措施
                 */

                private String curPm;
                private String pm25;
                private String pm10;
                private String level;
                private String quality;
                private String des;

                public String getCurPm() {
                    return curPm;
                }

                public void setCurPm(String curPm) {
                    this.curPm = curPm;
                }

                public String getPm25() {
                    return pm25;
                }

                public void setPm25(String pm25) {
                    this.pm25 = pm25;
                }

                public String getPm10() {
                    return pm10;
                }

                public void setPm10(String pm10) {
                    this.pm10 = pm10;
                }

                public String getLevel() {
                    return level;
                }

                public void setLevel(String level) {
                    this.level = level;
                }

                public String getQuality() {
                    return quality;
                }

                public void setQuality(String quality) {
                    this.quality = quality;
                }

                public String getDes() {
                    return des;
                }

                public void setDes(String des) {
                    this.des = des;
                }
            }
        }

        public static class WeatherBeanX {
            /**
             * date : 2017-08-16
             * week : 三
             * nongli : 闰六月廿五
             * info : {"dawn":null,"day":["4","雷阵雨","30","北风","微风","05:28","出门记得带伞，行走驾驶做好防滑准备"],"night":["2","阴","22","北风","微风","19:09","出门记得带伞，行走驾驶做好防滑准备"]}
             */

            private String date;
            private String week;
            private String nongli;
            private InfoBeanX info;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getWeek() {
                return week;
            }

            public void setWeek(String week) {
                this.week = week;
            }

            public String getNongli() {
                return nongli;
            }

            public void setNongli(String nongli) {
                this.nongli = nongli;
            }

            public InfoBeanX getInfo() {
                return info;
            }

            public void setInfo(InfoBeanX info) {
                this.info = info;
            }

            public static class InfoBeanX {
                /**
                 * dawn : null
                 * day : ["4","雷阵雨","30","北风","微风","05:28","出门记得带伞，行走驾驶做好防滑准备"]
                 * night : ["2","阴","22","北风","微风","19:09","出门记得带伞，行走驾驶做好防滑准备"]
                 */

                private Object dawn;
                private List<String> day;
                private List<String> night;

                public Object getDawn() {
                    return dawn;
                }

                public void setDawn(Object dawn) {
                    this.dawn = dawn;
                }

                public List<String> getDay() {
                    return day;
                }

                public void setDay(List<String> day) {
                    this.day = day;
                }

                public List<String> getNight() {
                    return night;
                }

                public void setNight(List<String> night) {
                    this.night = night;
                }
            }
        }
    }
}

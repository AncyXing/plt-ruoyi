# 基础镜像
FROM redis

# 挂载目录
VOLUME /home/plt/redis
# 创建目录
RUN mkdir -p /home/plt/redis
# 指定路径
WORKDIR /home/plt/redis
# 复制conf文件到路径
COPY ./conf/redis.conf /home/ruoyi/redis/redis.conf